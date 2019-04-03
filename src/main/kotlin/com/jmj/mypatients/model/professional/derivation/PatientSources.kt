package com.jmj.mypatients.model.professional.derivation

interface PatientSources {
    fun find(patientSourceId: Long): PatientSource?
    fun findByIdAndProfessionalId(patientSourceId: Long, professionalId: Long): PatientSource?
}