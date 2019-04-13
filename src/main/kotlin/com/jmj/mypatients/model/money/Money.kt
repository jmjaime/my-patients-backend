package com.jmj.mypatients.model.money

import java.math.BigDecimal

data class Money(val value: BigDecimal) {

    constructor(value: Int) : this(value.toBigDecimal())

    init {
        check(value >= BigDecimal.ZERO)
    }

}