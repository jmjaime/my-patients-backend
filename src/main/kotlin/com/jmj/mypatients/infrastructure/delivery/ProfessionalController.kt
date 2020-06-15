package com.jmj.mypatients.infrastructure.delivery

import com.jmj.mypatients.actions.FindProfessionalAction
import com.jmj.mypatients.actions.FindProfessionalRequest
import com.jmj.mypatients.actions.models.ProfessionalModel
import com.jmj.mypatients.infrastructure.myPatientsApiV1BasePath
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

data class ProfessionalResource(val id: String, val name: String)

private fun ProfessionalModel.toResource() = ProfessionalResource(this.id, this.name)



