package com.jmj.mypatients.infrastructure.persistence.treatment.derivation

import com.jmj.mypatients.domain.professional.derivation.PatientSource
import com.jmj.mypatients.domain.professional.derivation.PatientSources

class InMemoryPatientSources(private val patientSources: MutableMap<String, PatientSource> = mutableMapOf()) : PatientSources {

    override fun findByProfessionalId(professionalId: String) = patientSources.values.filter { it.professionalId == professionalId }

    override fun save(patientSource: PatientSource) {
        patientSources[patientSource.id] = patientSource
    }

    override fun findByIdAndProfessionalId(patientSourceId: String, professionalId: String): PatientSource? = patientSources.values.firstOrNull { it.id == patientSourceId && it.professionalId == professionalId }

    override fun find(patientSourceId: String): PatientSource? = patientSources[patientSourceId]
}