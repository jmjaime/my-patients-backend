package com.jmj.mypatients.model.treatment.session

import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.office.Office
import java.time.LocalDate

class Session(val number:Int, val date: LocalDate, val office: Office, val fee: Money, val paid: Boolean) {

}