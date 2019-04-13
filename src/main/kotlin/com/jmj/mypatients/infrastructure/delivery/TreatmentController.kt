package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.model.actions.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping( "/api/v1/treatments")
class TreatmentController(private val initTreatmentAction: InitTreatmentAction, private val findTreatmentsAction: FindTreatmentsAction) {

    private val professionalId = "1"

    @PostMapping
    fun createTreatment(@RequestBody newTreatmentRequest: NewTreatmentRequest): ResponseEntity<TreatmentModel> =
        return with(newTreatmentRequest) {
            val initTreatmentRequest = InitTreatmentRequest(professionalId, officeId, patientSourceId, patientId)
            return ResponseEntity.ok(initTreatmentAction.execute(initTreatmentRequest))
        }

    @GetMapping
    fun getTreatments(): ResponseEntity<List<TreatmentModel>> = ResponseEntity.ok(findTreatmentsAction.execute(FindTreatmentsRequest(professionalId)))
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class NewTreatmentRequest(val professionalId: String, val officeId: String, val patientSourceId: String, val patientId: String)