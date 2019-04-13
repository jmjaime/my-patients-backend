package com.jmj.mypatients.infrastructure.treatment.derivation

import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources

class InMemoryPatientSources(private val patientSources: MutableMap<String, PatientSource> = mutableMapOf()) : PatientSources {
    override fun save(patientSource: PatientSource) {
        patientSources[patientSource.id] = patientSource
    }

    override fun findByIdAndProfessionalId(patientSourceId: String, professionalId: String): PatientSource? = patientSources.values.firstOrNull { it.id == patientSourceId && it.professionalId == professionalId }

    override fun find(patientSourceId: String): PatientSource? = patientSources[patientSourceId]
}