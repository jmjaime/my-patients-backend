package com.jmj.mypatients.domain.professional.office

import com.jmj.mypatients.domain.money.Money

data class Office(val id: String,
                  val name: String,
                  val professionalId: String,
                  val cost: Money) {

    companion object {
        const val DEFUALT_OFFICE = "VIRTUAL"
        fun virtual(id: String, professionalId: String) = Office(id, DEFUALT_OFFICE, professionalId, Money.ZERO)
    }

    init {
        require(id.isNotBlank()) { "Id can not be blank" }
        require(name.isNotBlank()) { "Name can not be blank" }
        require(professionalId.isNotBlank()) { "Professional Id can not be blank" }
    }
}
