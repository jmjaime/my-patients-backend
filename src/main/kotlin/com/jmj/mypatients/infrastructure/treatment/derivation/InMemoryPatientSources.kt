package com.jmj.mypatients.infrastructure.treatment.derivation

import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources

class InMemoryPatientSources(private val patientSources: MutableMap<Long, PatientSource> = mutableMapOf()) : PatientSources {

    override fun findByIdAndProfessionalId(patientSourceId: Long, professionalId: Long): PatientSource? = patientSources.values.firstOrNull { it.id == patientSourceId && it.professionalId == professionalId }

    override fun find(patientSourceId: Long): PatientSource? = patientSources[patientSourceId]
}