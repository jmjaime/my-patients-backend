package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.treatment.session.Session
import java.math.BigDecimal
import java.time.LocalDate

data class SessionModel(val treatmentId: String,
                        val number: Int,
                        val date: LocalDate,
                        val officeId: String,
                        val fee: BigDecimal,
                        val paid: Boolean)


fun Session.toModel(treatmentId: String) = SessionModel(treatmentId, this.number, this.date, this.officeId, this.fee.value, this.paid)