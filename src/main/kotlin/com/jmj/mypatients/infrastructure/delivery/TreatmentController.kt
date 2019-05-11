package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
import com.jmj.mypatients.model.actions.FindTreatmentsAction
import com.jmj.mypatients.model.actions.FindTreatmentsRequest
import com.jmj.mypatients.model.actions.InitTreatmentAction
import com.jmj.mypatients.model.actions.InitTreatmentRequest
import com.jmj.mypatients.model.actions.models.TreatmentModel
import com.jmj.mypatients.model.actions.models.TreatmentSmallModel
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
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
                ResponseEntity.ok(initTreatmentAction(initTreatmentRequest).toResource(professionalId))
            }

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getTreatments(@PathVariable professionalId: String): ResponseEntity<List<TreatmentResource>> =
            ResponseEntity.ok(findTreatmentsAction(FindTreatmentsRequest(professionalId)).map { it.toResource(professionalId) })

    @GetMapping("/{treatmentId}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getTreatment(@PathVariable professionalId: String, @PathVariable treatmentId: String) = ResponseEntity.notFound()
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class NewTreatmentRequest(val officeId: String, val patientSourceId: String, val patient: String)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TreatmentResource(val id: String,
                             val patient: String,
                             val officeDescription: String? = null,
                             val patientSourceName: String? = null) : ResourceSupport()

private fun TreatmentSmallModel.toResource(professionalId: String) = TreatmentResource(id, patient).apply {
    add(linkTo(methodOn(TreatmentController::class.java).getTreatment(professionalId, id)).withSelfRel())
    add(linkTo(methodOn(SessionController::class.java).getSessions(professionalId, id)).withRel("sessions"))
}

private fun TreatmentModel.toResource(professionalId: String) = TreatmentResource(id, patient, office.description).apply {
    add(linkTo(methodOn(TreatmentController::class.java).getTreatment(professionalId, id)).withSelfRel())
    add(linkTo(methodOn(SessionController::class.java).getSessions(professionalId, id)).withRel("sessions"))
    add(linkTo(methodOn(OfficeController::class.java).getOffice(professionalId, office.id)).withRel("office"))
    add(linkTo(methodOn(PatientSourceController::class.java).getPatientSource(professionalId, derivation.patientSourceId)).withRel("patientSource"))
}

