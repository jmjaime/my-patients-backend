package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.patient.PatientFinder
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService

class InitTreatmentAction(private val treatmentService: TreatmentService, private val professionalFinder: ProfessionalFinder, private val patientFinder: PatientFinder) {

    fun execute(initTreatmentRequest: InitTreatmentRequest): TreatmentModel {
        with(initTreatmentRequest) {
            val professional = professionalFinder.findById(professionalId)
            val defaultOffice = professionalFinder.findProfessionalOfficeById(professional, officeId)
            val patientSource = professionalFinder.findProfessionalPatientSourceById(professional, patientSourceId)
            val patient = patientFinder.findPatientByProfessional(patientId, professional)
            return treatmentService.initTreatment(professional, patient, patientSource, defaultOffice).toModel()
        }
    }

}

data class InitTreatmentRequest(val professionalId: Long, val officeId: Long, val patientSourceId: Long, val patientId: Long)

