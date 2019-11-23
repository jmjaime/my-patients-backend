package com.jmj.mypatients.domain.money

import java.math.BigDecimal

data class Money(val value: BigDecimal) {
    operator fun plus(anotherMoney: Money) = Money(value + anotherMoney.value)
    operator fun minus(anotherMoney: Money) = Money(value - anotherMoney.value)
    fun negate() = Money(value.negate())
    operator fun times(times: Int) = Money(value.multiply(BigDecimal.valueOf(times.toLong())))

    companion object {
        val ZERO = Money(BigDecimal.ZERO)
    }

    constructor(value: Int) : this(value.toBigDecimal())

}