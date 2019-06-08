package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.actions.AddSessionAction
import com.jmj.mypatients.actions.AddSessionRequest
import com.jmj.mypatients.actions.models.SessionModel
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant

@RestController
@RequestMapping("$myPatientsApiV1BasePath/treatments/{treatmentId}/sessions")
class SessionController(private val addSessionAction: AddSessionAction) {

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getSessions(@PathVariable professionalId: String, @PathVariable treatmentId: String): ResponseEntity<List<SessionResource>> = ResponseEntity.ok(listOf())

    @GetMapping("/{sessionId}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getSession(@PathVariable professionalId: String, @PathVariable treatmentId: String, @PathVariable sessionNumber: Int) = ResponseEntity.notFound()


    @PostMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun createSession(@PathVariable professionalId: String, @PathVariable treatmentId: String, @RequestBody newSessionRequest: NewSessionRequest): ResponseEntity<SessionResource> =
            with(newSessionRequest) {
                val addSessionRequest = AddSessionRequest(professionalId, treatmentId, officeId, date, fee, paid)
                ResponseEntity.ok(addSessionAction(addSessionRequest).toResource(professionalId))
            }
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class NewSessionRequest(val officeId: String,
                             @JsonFormat(shape = JsonFormat.Shape.NUMBER) val date: Instant,
                             val fee: BigDecimal,
                             val paid: Boolean)


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class SessionResource(val number: Int,
                           @JsonFormat(shape = JsonFormat.Shape.NUMBER) val date: Instant,
                           val fee: BigDecimal,
                           val paid: Boolean) : ResourceSupport()

private fun SessionModel.toResource(professionalId: String) = SessionResource(number, date, fee, paid).apply {
    add(linkTo(methodOn(SessionController::class.java).getSession(professionalId, treatmentId, number)).withSelfRel())
    add(linkTo(methodOn(OfficeController::class.java).getOffice(professionalId, officeId)).withRel("office"))
    add(linkTo(methodOn(TreatmentController::class.java).getTreatment(professionalId, treatmentId)).withRel("treatment"))
    add(linkTo(methodOn(ProfessionalController::class.java).getMySelf(professionalId)).withRel("professional"))

}