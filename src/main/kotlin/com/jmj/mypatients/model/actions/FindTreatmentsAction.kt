package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.professional.ProfessionalFinder

class FindTreatmentsAction(private val professionalFinder: ProfessionalFinder) {

    fun execute(findTreatmentsRequest: FindTreatmentsRequest): List<TreatmentModel> {
        val professional = professionalFinder.findById(findTreatmentsRequest.professionalId)
        return professionalFinder.findTreatmentByProfessional(professional).map { it.toModel() }
    }
}

data class FindTreatmentsRequest(val professionalId: String)
