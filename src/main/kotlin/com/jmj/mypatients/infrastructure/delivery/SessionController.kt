package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.annotation.JsonFormat
import com.jmj.mypatients.actions.AddSessionAction
import com.jmj.mypatients.actions.AddSessionRequest
import com.jmj.mypatients.actions.models.SessionModel
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
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
                ResponseEntity.ok(addSessionAction(addSessionRequest).toResource())
            }
}

data class NewSessionRequest(val officeId: String,
                             @JsonFormat(shape = JsonFormat.Shape.NUMBER) val date: Instant,
                             val fee: BigDecimal,
                             val paid: Boolean)


data class SessionResource(val number: Int,
                           @JsonFormat(shape = JsonFormat.Shape.NUMBER) val date: Instant,
                           val fee: BigDecimal,
                           val paid: Boolean)

private fun SessionModel.toResource() = SessionResource(number, date, fee, paid)