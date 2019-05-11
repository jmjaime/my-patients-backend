package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.actions.models.OfficeModel
import com.jmj.mypatients.model.actions.models.toModel
import com.jmj.mypatients.model.professional.ProfessionalFinder

class FindOfficesAction(val professionalFinder: ProfessionalFinder) {

    operator fun invoke(findOfficesRequest: FindOfficesRequest): List<OfficeModel> {
        val professional = professionalFinder.findProfessionalById(findOfficesRequest.professionalId)
        return professionalFinder.findOfficesByProfessional(professional).map { it.toModel() }
    }
}

data class FindOfficesRequest(val professionalId: String)