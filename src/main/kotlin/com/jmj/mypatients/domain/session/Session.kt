package com.jmj.mypatients.domain.session

import com.jmj.mypatients.domain.money.Money
import java.time.Instant

// TODO: add total money
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