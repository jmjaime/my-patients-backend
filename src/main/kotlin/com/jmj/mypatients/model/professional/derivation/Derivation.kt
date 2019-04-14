package com.jmj.mypatients.model.professional.derivation

import com.jmj.mypatients.model.money.Money

data class Derivation(val patientSourceId: String, val currentFee: Money) {
    init {
        require( patientSourceId.isNotBlank()) {"Id can not be blank"}
    }
}