package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
import com.jmj.mypatients.model.actions.FindPatientSourcesAction
import com.jmj.mypatients.model.actions.FindProfessionalRequest
import com.jmj.mypatients.model.actions.models.PatientSourceModel
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("$myPatientsApiV1BasePath/patient-sources")
class PatientSourceController(private val findPatientSourcesAction: FindPatientSourcesAction) {

    @GetMapping("/{patientSourceId}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getPatientSource(@PathVariable professionalId: String, @PathVariable patientSourceId: String) = ResponseEntity.notFound()

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getPatientSources(@PathVariable professionalId: String) =
            ResponseEntity.ok(findPatientSourcesAction(FindProfessionalRequest(professionalId)).map { it.toResource(professionalId) })
}


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class PatientSourceResource(val id: String, val source: String, val fee: BigDecimal, val tax: BigDecimal) : ResourceSupport()

private fun PatientSourceModel.toResource(professionalId: String) = PatientSourceResource(id, source, fee, tax).apply {
    add(linkTo(methodOn(PatientSourceController::class.java).getPatientSource(professionalId, id)).withSelfRel())
}