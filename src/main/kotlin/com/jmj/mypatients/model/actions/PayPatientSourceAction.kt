package com.jmj.mypatients.model.actions

import java.math.BigDecimal

class PayPatientSourceAction {

    operator fun invoke(payPatientSourceRequest: PayPatientSourceRequest) {

    }

}

data class PayPatientSourceRequest(val professionalId: String,
                                   val patientSourceId: String,
                                   val value: BigDecimal)