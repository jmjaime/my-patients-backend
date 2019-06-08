package com.jmj.mypatients.actions

import java.math.BigDecimal

class PaySessionsAction {

    operator fun invoke(paySessionsRequest: PaySessionsRequest) {

    }
}


data class PaySessionsRequest(val professionalId: String,
                              val treatmentId: String,
                              val sessions: List<Int>,
                              val value: BigDecimal)