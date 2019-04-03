package com.jmj.mypatients.model.money

class Money(val value: Double) {

    constructor(value: Int) : this(value.toDouble())

}