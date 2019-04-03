package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.treatment.session.Session
import java.time.LocalDate

data class SessionModel(val number:Int, val date: LocalDate, val office: Long, val fee: Double, val paid: Boolean)


fun Session.toModel() = SessionModel(this.number, this.date, this.office.id, this.fee.value, this.paid)