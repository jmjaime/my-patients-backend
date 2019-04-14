package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.professional.ProfessionalFinder

class FindTreatmentsAction(private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(findTreatmentsRequest: FindTreatmentsRequest): List<TreatmentSmallModel> {
        val professional = professionalFinder.findById(findTreatmentsRequest.professionalId)
        return professionalFinder.findTreatmentByProfessional(professional).map { it.toSmallModel() }
    }
}

data class FindTreatmentsRequest(val professionalId: String)
