package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
import com.jmj.mypatients.model.actions.FindOfficesAction
import com.jmj.mypatients.model.actions.FindOfficesRequest
import com.jmj.mypatients.model.actions.models.OfficeModel
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$myPatientsApiV1BasePath/offices")
class OfficeController(private val findOfficesAction: FindOfficesAction) {

    @GetMapping("/{officeId}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getOffice(@PathVariable professionalId: String, @PathVariable officeId: String) = ResponseEntity.notFound()

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getOffices(@PathVariable professionalId: String) =
            ResponseEntity.ok(findOfficesAction(FindOfficesRequest(professionalId)).map { it.toResource(professionalId) })

}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class OfficeResource(val id: String, val description: String) : ResourceSupport()

private fun OfficeModel.toResource(professionalId: String) = OfficeResource(id, description).apply {
    add(linkTo(methodOn(OfficeController::class.java).getOffice(professionalId, id)).withSelfRel())
}