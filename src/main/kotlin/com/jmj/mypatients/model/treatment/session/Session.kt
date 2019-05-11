package com.jmj.mypatients.model.treatment.session

import com.jmj.mypatients.model.money.Money
import java.time.Instant

data class Session(val number: Int,
                   val date: Instant,
                   val officeId: String,
                   val fee: Money,
                   val paid: Boolean) {

    init {
        require( number > 0) {"Session number should be positive"}
        require( officeId.isNotBlank()) {"Name can not be blank"}
    }
}