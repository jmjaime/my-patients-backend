package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.actions.models.TreatmentSmallModel
import com.jmj.mypatients.model.actions.models.toSmallModel
import com.jmj.mypatients.model.professional.ProfessionalFinder

class FindTreatmentsAction(private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(findTreatmentsRequest: FindTreatmentsRequest): List<TreatmentSmallModel> {
        val professional = professionalFinder.findProfessionalById(findTreatmentsRequest.professionalId)
        return professionalFinder.findTreatmentsByProfessional(professional).map { it.toSmallModel() }
    }
}

data class FindTreatmentsRequest(val professionalId: String)
