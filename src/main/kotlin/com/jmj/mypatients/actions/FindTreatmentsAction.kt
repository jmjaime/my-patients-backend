package com.jmj.mypatients.actions

import com.jmj.mypatients.actions.models.TreatmentSmallModel
import com.jmj.mypatients.actions.models.toSmallModel
import com.jmj.mypatients.domain.professional.ProfessionalFinder

class FindTreatmentsAction(private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(findTreatmentsRequest: FindTreatmentsRequest): List<TreatmentSmallModel> {
        val professional = professionalFinder.findProfessionalById(findTreatmentsRequest.professionalId)
        return professionalFinder.findTreatmentsByProfessional(professional).map { it.toSmallModel() }
    }
}

data class FindTreatmentsRequest(val professionalId: String)
