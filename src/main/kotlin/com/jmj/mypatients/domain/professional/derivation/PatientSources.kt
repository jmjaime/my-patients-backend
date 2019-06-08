package com.jmj.mypatients.domain.professional.derivation

interface PatientSources {
    fun find(patientSourceId: String): PatientSource?
    fun findByIdAndProfessionalId(patientSourceId: String, professionalId: String): PatientSource?
    fun save(patientSource: PatientSource)
    fun findByProfessionalId(professionalId: String): List<PatientSource>
}