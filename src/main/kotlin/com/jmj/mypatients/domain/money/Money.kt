package com.jmj.mypatients.domain.money

import java.math.BigDecimal

data class Money(val value: BigDecimal) {
    operator fun plus(anotherMoney: Money) = Money(value + anotherMoney.value)
    operator fun minus(anotherMoney: Money) = Money(value - anotherMoney.value)
    fun negate() = Money(value.negate())

    companion object {
        val ZERO = Money(BigDecimal.ZERO)
    }

    constructor(value: Int) : this(value.toBigDecimal())

}