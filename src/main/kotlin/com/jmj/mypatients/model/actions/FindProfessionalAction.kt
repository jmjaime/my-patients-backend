package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.actions.models.ProfessionalModel
import com.jmj.mypatients.model.actions.models.toModel
import com.jmj.mypatients.model.professional.ProfessionalFinder

class FindProfessionalAction(private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(findProfessionalRequest: FindProfessionalRequest): ProfessionalModel =
            professionalFinder.findProfessionalById(findProfessionalRequest.professionalId).toModel()
}

data class FindProfessionalRequest(val professionalId:String)
