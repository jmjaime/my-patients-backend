package com.jmj.mypatients.model.professional.account

import com.google.common.eventbus.Subscribe
import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.events.SessionCreated
import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts
import com.jmj.mypatients.model.treatment.Treatment
import com.jmj.mypatients.model.treatment.session.Session
import java.time.Clock
import java.time.Instant

class ProfessionalAccountService(private val accounts: Accounts,
                                 private val officeAccounts: OfficeAccounts,
                                 private val patientSourceAccounts: PatientSourceAccounts,
                                 private val professionalFinder: ProfessionalFinder,
                                 private val clock: Clock) {

    @Subscribe
    fun onSessionCreated(sessionCreated: SessionCreated) {
        registerNewSession(sessionCreated.professional, sessionCreated.treatment, sessionCreated.sessionNumber)
    }


    fun registerNewSession(professionalId: String, treatmentId: String, sessionNumber: Int) {
        val professional = professionalFinder.findProfessionalById(professionalId)
        val treatment = professionalFinder.findTreatmentByProfessionalAndId(professional, treatmentId)
        val session = treatment.getSession(sessionNumber)
        val now = clock.instant()

        registerOnProfessionalAccount(professional, session, now)
        registerOnOfficeAccount(professional, session, now, treatment)
        registerOnPatientSourceAccount(professional, session, now, treatment)
    }

    private fun registerOnPatientSourceAccount(professional: Professional, session: Session, now: Instant, treatment: Treatment) {
        val patientSourceAccount = findPatientSourceAccountByProfessionalAndId(professional, treatment.derivation.patientSourceId)
        patientSourceAccount.addSessionCompleted(now, treatment.derivation.currentFee, treatment.id, session.number)
        patientSourceAccounts.save(patientSourceAccount)
    }

    private fun registerOnOfficeAccount(professional: Professional, session: Session, now: Instant, treatment: Treatment) {
        val office = professionalFinder.findOfficeByProfessionalAndId(professional, session.officeId)
        val officeAccount = findOfficeAccountByProfessionalAndId(professional, office.id)
        officeAccount.addOfficeUse(now, office.cost, treatment.id, session.number)
        officeAccounts.save(officeAccount)
    }

    private fun registerOnProfessionalAccount(professional: Professional, session: Session, now: Instant) {
        val account = findProfessionalAccount(professional)
        if (session.paid) {
            account.addCredit(now, session.fee)
        }
        accounts.save(account)
    }

    private fun findProfessionalAccount(professional: Professional) =
            accounts.findByProfessional(professional.id)
                    ?: throw ObjectNotFoundException("Professional account for professional ${professional.id} not found")

    private fun findOfficeAccountByProfessionalAndId(professional: Professional, officeId: String) =
            officeAccounts.findByProfessionalAndId(professional.id, officeId)
                    ?: throw ObjectNotFoundException("Office account for professional ${professional.id} and office $officeId not found")

    private fun findPatientSourceAccountByProfessionalAndId(professional: Professional, patientSourceId: String) =
            patientSourceAccounts.findByProfessionalAndId(professional.id, patientSourceId)
                    ?: throw ObjectNotFoundException("Patient Source account for professional ${professional.id} and $patientSourceId not found")

}