package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
import com.jmj.mypatients.model.actions.FindProfessionalAction
import com.jmj.mypatients.model.actions.FindProfessionalRequest
import com.jmj.mypatients.model.actions.ProfessionalModel
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(myPatientsApiV1BasePath)
class ProfessionalController(private val findProfessionalAction: FindProfessionalAction) {

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getMySelf(@PathVariable professionalId: String) =
            ResponseEntity.ok(findProfessionalAction(FindProfessionalRequest(professionalId)).toResource())
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class ProfessionalResource(val id: String, val name: String) : ResourceSupport()

private fun ProfessionalModel.toResource() = ProfessionalResource(this.id, this.name).apply {
    this.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OfficeController::class.java).getOffices(id)).withRel("offices"))
    this.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TreatmentController::class.java).getTreatments(id)).withRel("treatments"))
    this.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PatientSourceController::class.java).getPatientSources(id)).withRel("patient_sources"))

}



