package com.jmj.mypatients.model.professional.account

import com.google.common.eventbus.Subscribe
import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.events.SessionCreated
import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccount
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.model.professional.account.office.OfficeAccount
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccount
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccounts
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.treatment.Treatment
import com.jmj.mypatients.model.treatment.session.Session
import java.time.Clock

class ProfessionalAccountService(private val professionalAccounts: ProfessionalAccounts,
                                 private val officeAccounts: OfficeAccounts,
                                 private val patientSourceAccounts: PatientSourceAccounts,
                                 private val professionalFinder: ProfessionalFinder,
                                 private val clock: Clock) {

    @Subscribe
    fun onSessionCreated(sessionCreated: SessionCreated) {
        // TODO: extract to an action
        registerNewSession(sessionCreated.professional, sessionCreated.treatment, sessionCreated.sessionNumber)
    }

    fun registerOfficePayment(professional: Professional, office: Office, moneyOperation: MoneyOperation) {
        val professionalAccount = findProfessionalAccount(professional)
        val officeAccount = findOfficeAccountByProfessionalAndId(professional, office.id)
        debitOn(professionalAccount, moneyOperation)
        payTo(officeAccount, moneyOperation)
    }

    fun registerPatientSourcePayment(professional: Professional, patientSource: PatientSource, moneyOperation: MoneyOperation) {
        val professionalAccount = findProfessionalAccount(professional)
        val patientSourceAccount = findPatientSourceAccountByProfessionalAndId(professional, patientSource.id)
        debitOn(professionalAccount, moneyOperation)
        payTo(patientSourceAccount, moneyOperation)
    }

    fun registerNewSession(professionalId: String, treatmentId: String, sessionNumber: Int) {
        // TODO: change parameters to domain objects
        val professional = professionalFinder.findProfessionalById(professionalId)
        val treatment = professionalFinder.findTreatmentByProfessionalAndId(professional, treatmentId)
        val session = treatment.getSession(sessionNumber)

        registerNewSessionOnProfessionalAccount(professional, session)
        registerNewSessionOnOfficeAccount(professional, session)
        registerNewSessionOnPatientSourceAccount(professional, session, treatment)
    }

    private fun payTo(officeAccount: OfficeAccount, moneyOperation: MoneyOperation) {
        officeAccount.pay(moneyOperation)
        officeAccounts.save(officeAccount)
    }

    private fun debitOn(professionalAccount: ProfessionalAccount, moneyOperation: MoneyOperation) {
        professionalAccount.addDebit(moneyOperation)
        professionalAccounts.save(professionalAccount)
    }

    private fun payTo(patientSourceAccount: PatientSourceAccount, moneyOperation: MoneyOperation) {
        patientSourceAccount.pay(moneyOperation)
        patientSourceAccounts.save(patientSourceAccount)
    }

    private fun registerNewSessionOnPatientSourceAccount(professional: Professional, session: Session, treatment: Treatment) {
        val patientSourceAccount = findPatientSourceAccountByProfessionalAndId(professional, treatment.derivation.patientSourceId)
        patientSourceAccount.addTax(MoneyOperation(treatment.derivation.currentFee, session.date))
        patientSourceAccounts.save(patientSourceAccount)
    }

    private fun registerNewSessionOnOfficeAccount(professional: Professional, session: Session) {
        val office = professionalFinder.findOfficeByProfessionalAndId(professional, session.officeId)
        val officeAccount = findOfficeAccountByProfessionalAndId(professional, office.id)
        officeAccount.addUse(MoneyOperation(office.cost, session.date))
        officeAccounts.save(officeAccount)
    }

    private fun registerNewSessionOnProfessionalAccount(professional: Professional, session: Session) {
        val account = findProfessionalAccount(professional)
        if (session.paid) {
            account.addCredit(MoneyOperation(session.fee, session.date))
        }
        professionalAccounts.save(account)
    }

    private fun findProfessionalAccount(professional: Professional) =
            professionalAccounts.findByProfessional(professional.id)
                    ?: throw ObjectNotFoundException("Professional account for professional ${professional.id} not found")

    private fun findOfficeAccountByProfessionalAndId(professional: Professional, officeId: String) =
            officeAccounts.findByProfessionalAndId(professional.id, officeId)
                    ?: throw ObjectNotFoundException("Account not found for office $officeId and professional ${professional.id}")

    private fun findPatientSourceAccountByProfessionalAndId(professional: Professional, patientSourceId: String) =
            patientSourceAccounts.findByProfessionalAndId(professional.id, patientSourceId)
                    ?: throw ObjectNotFoundException("Account not found for patient Source $patientSourceId and professional ${professional.id}")

}