package com.jmj.mypatients.actions.models

import com.jmj.mypatients.domain.professional.Professional

data class ProfessionalModel(val id: String, val name: String)

fun Professional.toModel() = ProfessionalModel(this.id, this.name)