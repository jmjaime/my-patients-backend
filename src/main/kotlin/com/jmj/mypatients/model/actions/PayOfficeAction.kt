package com.jmj.mypatients.model.actions

import java.math.BigDecimal

class PayOfficeAction {

    operator fun invoke(payOfficeRequest: PayOfficeRequest) {

    }
}

data class PayOfficeRequest(val professionalId: String,
                            val officeId: String,
                            val value: BigDecimal)