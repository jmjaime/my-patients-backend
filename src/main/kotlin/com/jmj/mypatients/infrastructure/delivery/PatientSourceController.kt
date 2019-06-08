package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.actions.FindPatientSourcesAction
import com.jmj.mypatients.actions.FindProfessionalRequest
import com.jmj.mypatients.actions.PayPatientSourceAction
import com.jmj.mypatients.actions.PayPatientSourceRequest
import com.jmj.mypatients.actions.models.PatientSourceModel
import com.jmj.mypatients.actions.models.PaymentModel
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
@RequestMapping("$myPatientsApiV1BasePath/patient-sources")
class PatientSourceController(private val findPatientSourcesAction: FindPatientSourcesAction, private val payPatientSourceAction: PayPatientSourceAction) {

    @GetMapping("/{patientSourceId}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getPatientSource(@PathVariable professionalId: String, @PathVariable patientSourceId: String) = ResponseEntity.notFound().build<Any>()

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getPatientSources(@PathVariable professionalId: String) =
            ResponseEntity.ok(findPatientSourcesAction(FindProfessionalRequest(professionalId)).map { it.toResource(professionalId) })

    @PostMapping("/{patientSourceId}/payments")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun payToPatientSource(@PathVariable professionalId: String, @PathVariable patientSourceId: String, @RequestBody patientSourcePaymentRequest: PatientSourcePaymentRequest) =
            ResponseEntity.created(payPatientSourceAction(PayPatientSourceRequest(professionalId, patientSourceId, patientSourcePaymentRequest.value, patientSourcePaymentRequest.date))
                    .toLocation(professionalId, patientSourceId)).build<Any>()

    @GetMapping("/{patientSourceId}/payments/{paymentNumber}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getPatientSourcePayment(@PathVariable professionalId: String, @PathVariable patientSourceId: String, @PathVariable paymentNumber: Int) = ResponseEntity.notFound().build<Any>()
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class PatientSourcePaymentRequest(val value: BigDecimal, val date: Instant)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class PatientSourceResource(val id: String, val source: String, val fee: BigDecimal, val tax: BigDecimal) : ResourceSupport()

private fun PatientSourceModel.toResource(professionalId: String) = PatientSourceResource(id, source, fee, tax).apply {
    add(linkTo(methodOn(PatientSourceController::class.java).getPatientSource(professionalId, id)).withSelfRel())
}

private fun PaymentModel.toLocation(professionalId: String, patientSourceId: String) =
        linkTo(methodOn(PatientSourceController::class.java).getPatientSourcePayment(professionalId, patientSourceId, this.number)).toUri()