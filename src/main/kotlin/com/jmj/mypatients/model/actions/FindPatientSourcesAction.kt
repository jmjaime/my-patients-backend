package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.actions.models.PatientSourceModel
import com.jmj.mypatients.model.actions.models.toModel
import com.jmj.mypatients.model.professional.ProfessionalFinder

class FindPatientSourcesAction(private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(findProfessionalRequest: FindProfessionalRequest): List<PatientSourceModel> {
        val professional = professionalFinder.findProfessionalById(findProfessionalRequest.professionalId)
        return professionalFinder.findPatientSourcesByProfessional(professional).map { it.toModel() }
    }
}

data class FindPatientSourcesRequest(val professionalId: String)