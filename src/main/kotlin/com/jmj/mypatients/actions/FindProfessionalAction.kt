package com.jmj.mypatients.actions

import com.jmj.mypatients.actions.models.ProfessionalModel
import com.jmj.mypatients.actions.models.toModel
import com.jmj.mypatients.domain.professional.ProfessionalFinder

class FindProfessionalAction(private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(findProfessionalRequest: FindProfessionalRequest): ProfessionalModel =
            professionalFinder.findProfessionalById(findProfessionalRequest.professionalId).toModel()
}

data class FindProfessionalRequest(val professionalId:String)
