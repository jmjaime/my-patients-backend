package com.jmj.mypatients.infrastructure.patient

import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.patient.Patients

class InMemoryPatients(private val patients: MutableMap<Long, Patient> = mutableMapOf()) : Patients {

    private var lastId = 0L

    override fun nextId(): Long {
        lastId = lastId.inc()
        return lastId
    }

    override fun find(patientId: Long): Patient? = patients[patientId]

    override fun findByIdAndProfessionalId(patientId: Long, professionalId: Long): Patient? = patients.values.first { it.id == patientId && it.professionalId == professionalId }

    override fun save(patient: Patient) {
        patients[patient.id] = patient
    }

}