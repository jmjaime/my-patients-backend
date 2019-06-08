package com.jmj.mypatients.actions.models

import com.jmj.mypatients.domain.treatment.session.Session
import java.math.BigDecimal
import java.time.Instant

data class SessionModel(val treatmentId: String,
                        val number: Int,
                        val date: Instant,
                        val officeId: String,
                        val fee: BigDecimal,
                        val paid: Boolean)


fun Session.toModel(treatmentId: String) = SessionModel(treatmentId, this.number, this.date, this.officeId, this.fee.value, this.paid)