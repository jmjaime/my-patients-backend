package com.jmj.mypatients.model.professional.account

import com.jmj.mypatients.model.money.Money
import java.time.Instant

data class Account(val professionalId: String,
                   private var credit: Money = Money.ZERO,
                   private var debit: Money = Money.ZERO,
                   private val movements: MutableList<Movement> = mutableListOf()) {

    fun credit() = credit

    fun debit() = debit

    fun movements() = movements.toList()

    fun addCredit(date: Instant, value: Money) = Movement.newCredit(nextMovement(), date, value)
            .also {
                credit += value
                movements.add(it)
            }

    private fun nextMovement() = movements.size + 1


}

data class Movement(val number: Int, val date: Instant, val value: Money, val movementType: MovementType) {
    companion object {
        fun newCredit(number: Int, date: Instant, value: Money) = Movement(number, date, value, MovementType.CREDIT)
    }
}

enum class MovementType {
    CREDIT,
    DEBIT
}