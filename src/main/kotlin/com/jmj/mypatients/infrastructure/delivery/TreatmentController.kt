package com.jmj.mypatients.infrastructure.delivery

import com.jmj.mypatients.actions.FindTreatmentsAction
import com.jmj.mypatients.actions.FindTreatmentsRequest
import com.jmj.mypatients.actions.InitTreatmentAction
import com.jmj.mypatients.actions.InitTreatmentRequest
import com.jmj.mypatients.actions.models.TreatmentModel
import com.jmj.mypatients.actions.models.TreatmentSmallModel
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("$myPatientsApiV1BasePath/treatments")
class TreatmentController(private val initTreatmentAction: InitTreatmentAction, private val findTreatmentsAction: FindTreatmentsAction) {

    @PostMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun createTreatment(@PathVariable professionalId: String, @RequestBody newTreatmentRequest: NewTreatmentRequest): ResponseEntity<TreatmentResource> =
            with(newTreatmentRequest) {
                val initTreatmentRequest = InitTreatmentRequest(professionalId, officeId, patientSourceId, patient)
                ResponseEntity.ok(initTreatmentAction(initTreatmentRequest).toResource())
            }

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getTreatments(@PathVariable professionalId: String): ResponseEntity<List<TreatmentResource>> =
            ResponseEntity.ok(findTreatmentsAction(FindTreatmentsRequest(professionalId)).map { it.toResource() })

    @GetMapping("/{treatmentId}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getTreatment(@PathVariable professionalId: String, @PathVariable treatmentId: String) = ResponseEntity.notFound()
}

data class NewTreatmentRequest(val officeId: String, val patientSourceId: String, val patient: String)

data class TreatmentResource(val id: String,
                             val patient: String,
                             val officeDescription: String? = null,
                             val patientSourceName: String? = null)

private fun TreatmentSmallModel.toResource() = TreatmentResource(id, patient)

private fun TreatmentModel.toResource() = TreatmentResource(id, patient, office.name)
