package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentFinder

class FindTreatments(private val professionalFinder: ProfessionalFinder, private val treatmentFinder: TreatmentFinder) {

    fun execute(findTreatmentsRequest: FindTreatmentsRequest): List<TreatmentModel> {
        val professional = professionalFinder.findById(findTreatmentsRequest.professionalId)
        return treatmentFinder.findTreatmentByProfessional(professional).map { it.toModel() }
    }
}

data class FindTreatmentsRequest(val professionalId: Long)
