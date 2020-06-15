package com.jmj.mypatients.domain.professional.account

import com.google.common.eventbus.Subscribe
import com.jmj.mypatients.domain.errors.ObjectNotFoundException
import com.jmj.mypatients.domain.events.SessionCreated
import com.jmj.mypatients.domain.professional.Professional
import com.jmj.mypatients.domain.professional.ProfessionalFinder
import com.jmj.mypatients.domain.professional.account.derivation.PatientSourceAccount
import com.jmj.mypatients.domain.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.domain.professional.account.office.OfficeAccount
import com.jmj.mypatients.domain.professional.account.office.OfficeAccounts
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccount
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccounts
import com.jmj.mypatients.domain.professional.derivation.PatientSource
import com.jmj.mypatients.domain.professional.office.Office
import com.jmj.mypatients.domain.session.Session
import com.jmj.mypatients.domain.treatment.Treatment
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

    fun registerOfficePayment(professional: Professional, office: Office, moneyOperation: MoneyOperation): Movement {
        val professionalAccount = findProfessionalAccount(professional)
        val officeAccount = findOfficeAccountByProfessionalAndId(professional, office.id)
        debitOn(professionalAccount, moneyOperation)
        return payTo(officeAccount, moneyOperation)
    }

    fun registerPatientSourcePayment(professional: Professional, patientSource: PatientSource, moneyOperation: MoneyOperation): Movement {
        val professionalAccount = findProfessionalAccount(professional)
        val patientSourceAccount = findPatientSourceAccountByProfessionalAndId(professional, patientSource.id)
        debitOn(professionalAccount, moneyOperation)
        return payTo(patientSourceAccount, moneyOperation)
    }

    fun registerSessionsPayment(professional: Professional, treatmentId: String, sessions: Set<Int>, moneyOperation: MoneyOperation): Movement {
        val professionalAccount = findProfessionalAccount(professional)
        val treatment = professionalFinder.findTreatmentByProfessionalAndId(professional, treatmentId)
        val sessionsNoPaid = treatment.getSessionsNotPaid()
        val sessionsToPay = sessionsNoPaid.filter { it.number in sessions }
        check(sessionsToPay.size == sessions.size) { "Sessions ${sessions - sessionsToPay.map { it.number }} not found to pay" }
        // TODO: check total to pay
        treatment.markAsPaid(sessionsToPay)
        return payTo(professionalAccount, moneyOperation)
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

    private fun payTo(officeAccount: OfficeAccount, moneyOperation: MoneyOperation) =
            officeAccount.pay(moneyOperation).also {
                officeAccounts.save(officeAccount)
            }

    private fun debitOn(professionalAccount: ProfessionalAccount, moneyOperation: MoneyOperation) =
            professionalAccount.addDebit(moneyOperation).also {
                professionalAccounts.save(professionalAccount)
            }

    private fun payTo(patientSourceAccount: PatientSourceAccount, moneyOperation: MoneyOperation) =
            patientSourceAccount.pay(moneyOperation).also {
                patientSourceAccounts.save(patientSourceAccount)
            }

    private fun payTo(professionalAccount: ProfessionalAccount, moneyOperation: MoneyOperation) =
            professionalAccount.addCredit(moneyOperation).also {
                professionalAccounts.save(professionalAccount)
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