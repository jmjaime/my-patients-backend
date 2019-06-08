package com.jmj.mypatients.domain.professional.account

import com.jmj.mypatients.domain.money.Money
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
                credit += moneyOperation.amount
                movements.add(it)
            }

    fun addDebit(moneyOperation: MoneyOperation) = Movement.newDebit(nextMovement(), moneyOperation)
            .also {
                debit += moneyOperation.amount
                movements.add(it)
            }

    private fun nextMovement() = movements.size + 1

}

data class Movement(val number: Int, val date: Instant, val amount: Money, val movementType: MovementType) {
    companion object {
        fun newCredit(number: Int, moneyOperation: MoneyOperation) = Movement(number, moneyOperation.date, moneyOperation.amount, MovementType.CREDIT)
        fun newDebit(number: Int, moneyOperation: MoneyOperation) = Movement(number, moneyOperation.date, moneyOperation.amount, MovementType.DEBIT)
    }
}

enum class MovementType {
    CREDIT,
    DEBIT
}