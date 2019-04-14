package com.jmj.mypatients.infrastructure.delivery

import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$myPatientsApiV1BasePath/offices")
class OfficeController {

    @GetMapping("/{officeId}")
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getOffice(@PathVariable professionalId: String, @PathVariable officeId: String)  = ResponseEntity.notFound()

    @GetMapping
    @PreAuthorize(PROFESSIONAL_PRE_AUTHORIZE)
    fun getOffices(@PathVariable professionalId: String) = ResponseEntity.ok(listOf<Any>())

}