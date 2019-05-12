package com.jmj.mypatients.model.actions.models

import com.jmj.mypatients.model.professional.account.Movement
import java.math.BigDecimal
import java.time.Instant

data class PaymentModel(val number: Int, val date: Instant, val amount: BigDecimal)

fun Movement.toPayment() = PaymentModel(this.number, this.date, this.amount.value)