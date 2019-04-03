package com.jmj.mypatients.infrastructure.treatment

import com.jmj.mypatients.model.treatment.Treatment
import com.jmj.mypatients.model.treatment.Treatments
import com.jmj.mypatients.model.patient.Patient

class InMemoryTreatments(private val treatments: MutableMap<Long, Treatment> = mutableMapOf()) : Treatments {
    private var lastId = 0L

    override fun find(treatmentId: Long): Treatment? = treatments[treatmentId]

    override fun save(treatment: Treatment) {
        treatments[treatment.id] = treatment
    }

    override fun nextId(): Long {
        lastId = lastId.inc()
        return lastId
    }

    override fun findByPatient(patient: Patient): Treatment? = treatments.values.find { patient.id == it.patientId }

    override fun findAll(): List<Treatment> = treatments.map { it.value }

    override fun findByIdAndProfessionalId(treatmentId: Long, professionalId: Long) = treatments.values.find { it.id == treatmentId && it.professionalId == professionalId }

    override fun findByProfessional(professionalId: Long): List<Treatment> = treatments.values.filter { it.professionalId == professionalId }

}
