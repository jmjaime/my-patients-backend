package com.jmj.mypatients.domain.professional.derivation

import com.jmj.mypatients.domain.money.Money

data class Derivation(val patientSourceId: String, val currentFee: Money) {
    init {
        require( patientSourceId.isNotBlank()) {"Id can not be blank"}
    }
}