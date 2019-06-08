package com.jmj.mypatients.domain.treatment

import com.jmj.mypatients.domain.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.domain.events.EventPublisher
import com.jmj.mypatients.domain.events.SessionCreated
import com.jmj.mypatients.domain.money.Money
import com.jmj.mypatients.domain.patient.Patient
import com.jmj.mypatients.domain.professional.Professional
import com.jmj.mypatients.domain.professional.derivation.Derivation
import com.jmj.mypatients.domain.professional.derivation.PatientSource
import com.jmj.mypatients.domain.professional.office.Office
import com.jmj.mypatients.domain.treatment.session.Session
import java.time.Instant

class TreatmentService(private val treatments: Treatments, private val eventPublisher: EventPublisher, private val idGenerator: () -> String) {

    fun initTreatment(professional: Professional, patientName: String, patientSource: PatientSource, defaultOffice: Office): Treatment {
        validatePatient(patientName)
        val patient = Patient(idGenerator(), patientName)
        val derivation = Derivation(patientSource.id, patientSource.fee)
        return Treatment(idGenerator.invoke(), professional.id, patient, defaultOffice.id, derivation).apply { treatments.save(this) }
    }

    fun newSession(treatment: Treatment, date: Instant, office: Office, fee: Money, paid: Boolean): Session {
        val session = treatment.addSession(date, office.id, fee, paid)
        treatments.save(treatment)
        notifySessionCreated(treatment, session)
        return session
    }

    private fun notifySessionCreated(treatment: Treatment, session: Session) {
        generateSessionCreatedEvent(treatment, session).let { eventPublisher.publish(it) }
    }

    private fun validatePatient(patientName: String) {
        treatments.findByPatientName(patientName)?.run {
            throw ObjectAlreadyExistsException("A treatment for patient $patientName already exits")
        }
    }

    private fun generateSessionCreatedEvent(treatment: Treatment, sessionCreated: Session) =
            SessionCreated(treatment.professionalId, treatment.id, sessionCreated.number)
}