package com.jmj.mypatients.actions

import com.jmj.mypatients.domain.professional.ProfessionalInitializer

class NewProfessionalAction(private val professionalInitializer: ProfessionalInitializer) {

    operator fun invoke(newProfessionalRequest: NewProfessionalRequest) {
        professionalInitializer.newProfessional(newProfessionalRequest.name)
    }
}

data class NewProfessionalRequest(val name: String)
