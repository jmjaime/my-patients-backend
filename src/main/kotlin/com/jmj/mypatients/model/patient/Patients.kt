package com.jmj.mypatients.model.patient

interface Patients {

    fun nextId(): Long

    fun find(patientId: Long): Patient?

    fun findByIdAndProfessionalId(patientId: Long, professionalId: Long): Patient?

    fun save(patient: Patient)

}