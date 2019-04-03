package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.model.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.derivation.Derivation
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.treatment.account.Account
import com.jmj.mypatients.model.treatment.session.Session
import java.time.LocalDate

class TreatmentService(private val treatments: Treatments) {

    fun initTreatment(professional: Professional, patient: Patient, patientSource: PatientSource, defaultOffice: Office): Treatment {
        validatePatient(patient)
        val derivation = Derivation(patientSource, patientSource.fee)
        return Treatment(treatments.nextId(), professional.id, patient.id, Account(), defaultOffice, derivation).apply { treatments.save(this) }
    }

    fun newSession(treatment: Treatment, date: LocalDate, office: Office, fee: Money, paid: Boolean): Session {
        val session = treatment.addSession(date, office, fee, paid)
        treatments.save(treatment)
        return session
    }

    private fun validatePatient(patient: Patient) {
        treatments.findByPatient(patient)?.run { throw ObjectAlreadyExistsException("A treatment for patient ${patient.name} already exits") }
    }


}