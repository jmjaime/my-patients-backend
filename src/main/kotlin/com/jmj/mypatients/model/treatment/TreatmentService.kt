package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.model.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.derivation.Derivation
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.treatment.session.Session
import java.time.LocalDate

class TreatmentService(private val treatments: Treatments, private val idGenerator: () -> String) {

    fun initTreatment(professional: Professional, patientName: String, patientSource: PatientSource, defaultOffice: Office): Treatment {
        validatePatient(patientName)
        val patient = Patient(idGenerator.invoke(), patientName)
        val derivation = Derivation(patientSource.id, patientSource.fee)
        return Treatment(idGenerator.invoke(), professional.id, patient, defaultOffice.id, derivation).apply { treatments.save(this) }
    }

    fun newSession(treatment: Treatment, date: LocalDate, office: Office, fee: Money, paid: Boolean): Session {
        val session = treatment.addSession(date, office.id, fee, paid)
        treatments.save(treatment)
        return session
    }

    private fun validatePatient(patientName: String) {
        treatments.findByPatientName(patientName)?.run { throw ObjectAlreadyExistsException("A treatment for patient ${patient.name} already exits") }
    }


}