package com.jmj.mypatients.model.professional.account.office

import com.jmj.mypatients.model.money.Money
import java.time.Instant

class OfficeAccount(val id: String,
                    val professionalId: String,
                    val officeId: String,
                    private var paid: Money = Money.ZERO,
                    private var used: Money = Money.ZERO,
                    private val movements: MutableList<OfficeMovement> = mutableListOf()) {

    fun paid() = paid

    fun used() = used

    fun movements() = movements.toList()

    fun addOfficeUse(date: Instant, cost: Money, treatment: String, sessionNumber: Int) =
            Use(nextMovement(), date, cost, treatment, sessionNumber).also {
                used += cost
                movements.add(it)
            }

    private fun nextMovement() = movements.size + 1

}

sealed class OfficeMovement
data class Use(val number: Int, val date: Instant, val value: Money, val treatment: String, val sessionNumber: Int) : OfficeMovement()
data class Payment(val number: Int, val date: Instant, val value: Money) : OfficeMovement()


