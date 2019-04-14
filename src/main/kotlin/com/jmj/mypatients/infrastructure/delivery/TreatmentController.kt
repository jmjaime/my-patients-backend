package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
import com.jmj.mypatients.model.actions.*
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("$myPatientsApiV1BasePath/treatments")
class TreatmentController(private val initTreatmentAction: InitTreatmentAction, private val findTreatmentsAction: FindTreatmentsAction) {

    @PostMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun createTreatment(@PathVariable professionalId: String, @RequestBody newTreatmentRequest: NewTreatmentRequest): ResponseEntity<TreatmentResource> =
            return with(newTreatmentRequest) {
                val initTreatmentRequest = InitTreatmentRequest(professionalId, officeId, patientSourceId, patientId)
                return ResponseEntity.ok(initTreatmentAction(initTreatmentRequest).toResource(professionalId))
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
data class NewTreatmentRequest(val officeId: String, val patientSourceId: String, val patientId: String)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TreatmentResource(val id: String,
                             val patient: String,
                             val officeDescription: String? = null,
                             val patientSourceName: String? = null) : ResourceSupport()

private fun TreatmentSmallModel.toResource(professionalId: String) = TreatmentResource(this.id, this.patient).apply {
    add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TreatmentController::class.java).getTreatment(professionalId, id)).withSelfRel())
    this.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SessionController::class.java).getSessions(professionalId, id)).withRel("sessions"))
}

private fun TreatmentModel.toResource(professionalId: String) = TreatmentResource(this.id, this.patient, this.office.description).apply {
    add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TreatmentController::class.java).getTreatment(professionalId, id)).withSelfRel())
    this.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SessionController::class.java).getSessions(professionalId, id)).withRel("sessions"))
    this.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OfficeController::class.java).getOffice(professionalId, office.id)).withRel("office"))
    this.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PatientSourceController::class.java).getPatientSource(professionalId, derivation.patientSourceId)).withRel("patientSource"))
}

