package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.model.actions.*

class TreatmentController(private val initTreatmentAction: InitTreatmentAction, private val findTreatments: FindTreatments) {

    private val professionalId = 1L

    fun createTreatment(newTreatment: NewTreatment): TreatmentModel {
        with(newTreatment) {
            val initTreatmentRequest = InitTreatmentRequest(professionalId, officeId, patientSourceId, patientId)
            return initTreatmentAction.execute(initTreatmentRequest)
        }
    }

    fun getTreatments(): List<TreatmentModel> = findTreatments.execute(FindTreatmentsRequest(professionalId))
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class NewTreatment(val professionalId: Long, val officeId: Long, val patientSourceId: Long, val patientId: Long)