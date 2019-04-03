package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.model.patient.Patient

interface Treatments {

    fun nextId(): Long

    fun find(treatmentId: Long): Treatment?

    fun save(treatment: Treatment)

    fun findByPatient(patient: Patient): Treatment?

    fun findAll(): List<Treatment>

    fun findByIdAndProfessionalId(treatmentId: Long, professionalId: Long): Treatment?

    fun findByProfessional(professionalId: Long): List<Treatment>

}