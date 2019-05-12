package com.jmj.mypatients.model.professional.account

import com.jmj.mypatients.model.money.Money
import java.time.Instant

data class Account(private var credit: Money = Money.ZERO,
                   private var debit: Money = Money.ZERO,
                   private val movements: MutableList<Movement> = mutableListOf()) {

    fun credit() = credit

    fun debit() = debit

    fun balance() = credit - debit

    fun movements() = movements.toList()

    fun addCredit(moneyOperation: MoneyOperation) = Movement.newCredit(nextMovement(), moneyOperation)
            .also {
                credit += moneyOperation.value
                movements.add(it)
            }

    fun addDebit(moneyOperation: MoneyOperation) = Movement.newDebit(nextMovement(), moneyOperation)
            .also {
                debit += moneyOperation.value
                movements.add(it)
            }

    private fun nextMovement() = movements.size + 1

}

data class Movement(val number: Int, val date: Instant, val value: Money, val movementType: MovementType) {
    companion object {
        fun newCredit(number: Int, moneyOperation: MoneyOperation) = Movement(number, moneyOperation.date, moneyOperation.value, MovementType.CREDIT)
        fun newDebit(number: Int, moneyOperation: MoneyOperation) = Movement(number, moneyOperation.date, moneyOperation.value, MovementType.DEBIT)
    }
}

enum class MovementType {
    CREDIT,
    DEBIT
}