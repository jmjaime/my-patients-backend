package com.jmj.mypatients.model.treatment

interface Treatments {

    fun find(treatmentId: String): Treatment?

    fun save(treatment: Treatment)

    fun findByPatientName(patientName: String): Treatment?

    fun findAll(): List<Treatment>

    fun findByIdAndProfessionalId(treatmentId: String, professionalId: String): Treatment?

    fun findByProfessional(professionalId: String): List<Treatment>

}