package com.jmj.mypatients.actions

import com.jmj.mypatients.actions.models.PatientSourceModel
import com.jmj.mypatients.actions.models.toModel
import com.jmj.mypatients.domain.professional.ProfessionalFinder

class FindPatientSourcesAction(private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(findProfessionalRequest: FindProfessionalRequest): List<PatientSourceModel> {
        val professional = professionalFinder.findProfessionalById(findProfessionalRequest.professionalId)
        return professionalFinder.findPatientSourcesByProfessional(professional).map { it.toModel() }
    }
}

data class FindPatientSourcesRequest(val professionalId: String)