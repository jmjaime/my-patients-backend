package com.jmj.mypatients.infrastructure.persistence.treatment

import com.jmj.mypatients.domain.treatment.Treatment
import com.jmj.mypatients.domain.treatment.Treatments

class InMemoryTreatments(private val treatments: MutableMap<String, Treatment> = mutableMapOf()) : Treatments {

    override fun find(treatmentId: String): Treatment? = treatments[treatmentId]

    override fun save(treatment: Treatment) {
        treatments[treatment.id] = treatment
    }


    override fun findByPatientName(patientName: String): Treatment? = treatments.values.find { patientName == it.patient.name }

    override fun findAll(): List<Treatment> = treatments.map { it.value }

    override fun findByIdAndProfessionalId(treatmentId: String, professionalId: String) = treatments.values.find { it.id == treatmentId && it.professionalId == professionalId }

    override fun findByProfessional(professionalId: String): List<Treatment> = treatments.values.filter { it.professionalId == professionalId }

}
