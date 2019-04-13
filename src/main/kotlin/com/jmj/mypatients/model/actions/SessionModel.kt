package com.jmj.mypatients.model.actions

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.model.treatment.session.Session
import java.math.BigDecimal
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class SessionModel(val number:Int, val date: LocalDate, val officeId: String, val fee: BigDecimal, val paid: Boolean)


fun Session.toModel() = SessionModel(this.number, this.date, this.officeId, this.fee.value, this.paid)