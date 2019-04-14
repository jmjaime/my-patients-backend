package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.professional.Professional

data class ProfessionalModel(val id: String, val name: String)

fun Professional.toModel() = ProfessionalModel(this.id, this.name)