package com.jmj.mypatients.model.patient

import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.professional.Professional

class PatientFinder(private val patients: Patients) {

    fun findPatientByProfessional(patientId: Long, professional: Professional): Patient =
        patients.findByIdAndProfessionalId(patientId, professional.id)
                ?: throw ObjectNotFoundException("Patient $patientId not found")

}