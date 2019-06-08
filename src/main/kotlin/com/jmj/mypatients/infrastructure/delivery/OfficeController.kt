package com.jmj.mypatients.infrastructure.delivery

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.actions.FindOfficesAction
import com.jmj.mypatients.actions.FindOfficesRequest
import com.jmj.mypatients.actions.PayOfficeAction
import com.jmj.mypatients.actions.PayOfficeRequest
import com.jmj.mypatients.actions.models.OfficeModel
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
@RequestMapping("$myPatientsApiV1BasePath/offices")
class OfficeController(private val findOfficesAction: FindOfficesAction, private val payOfficeAction: PayOfficeAction) {

    @GetMapping("/{officeId}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getOffice(@PathVariable professionalId: String, @PathVariable officeId: String) = ResponseEntity.notFound().build<Any>()

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getOffices(@PathVariable professionalId: String) =
            ResponseEntity.ok(findOfficesAction(FindOfficesRequest(professionalId)).map { it.toResource(professionalId) })

    @PostMapping("/{officeId}/payments")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun payToOffice(@PathVariable professionalId: String, @PathVariable officeId: String, @RequestBody officePaymentRequest: OfficePaymentRequest) =
            ResponseEntity.created(payOfficeAction(PayOfficeRequest(professionalId, officeId, officePaymentRequest.value, officePaymentRequest.date))
                    .toLocation(professionalId, officeId)).build<Any>()

    @GetMapping("/{officeId}/payments/{paymentNumber}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getOfficePayment(@PathVariable professionalId: String, @PathVariable officeId: String, @PathVariable paymentNumber: Int) = ResponseEntity.notFound().build<Any>()
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class OfficePaymentRequest(val value: BigDecimal, val date: Instant)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class OfficeResource(val id: String, val description: String) : ResourceSupport()

private fun OfficeModel.toResource(professionalId: String) = OfficeResource(id, description).apply {
    add(linkTo(methodOn(OfficeController::class.java).getOffice(professionalId, id)).withSelfRel())
}

private fun PaymentModel.toLocation(professionalId: String, officeId: String) =
        linkTo(methodOn(OfficeController::class.java).getOfficePayment(professionalId, officeId, this.number)).toUri()