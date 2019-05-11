package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.actions.models.TreatmentModel
import com.jmj.mypatients.model.actions.models.toModel
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService

class InitTreatmentAction(private val treatmentService: TreatmentService, private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(initTreatmentRequest: InitTreatmentRequest): TreatmentModel {
        with(initTreatmentRequest) {
            val professional = professionalFinder.findProfessionalById(professionalId)
            val defaultOffice = professionalFinder.findOfficeByProfessionalAndId(professional, officeId)
            val patientSource = professionalFinder.findPatientSourceByProfessionalAndId(professional, patientSourceId)
            return treatmentService.initTreatment(professional, patientName, patientSource, defaultOffice).toModel(defaultOffice)
        }
    }

}

data class InitTreatmentRequest(val professionalId: String, val officeId: String, val patientSourceId: String, val patientName: String)

