package com.jmj.mypatients.model.treatment.session

import com.jmj.mypatients.model.money.Money
import java.time.LocalDate

data class Session(val number:Int, val date: LocalDate, val officeId: String, val fee: Money, val paid: Boolean) {

    init {
        require( number > 0) {"Session number should be positive"}
        require( officeId.isNotBlank()) {"Name can not be blank"}
    }
}