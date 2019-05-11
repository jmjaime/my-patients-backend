package com.jmj.mypatients.model.professional.account.derivation

import com.jmj.mypatients.model.money.Money
import java.time.Instant

class PatientSourceAccount(val id: String,
                           val professionalId: String,
                           val patientSourceId: String,
                           private var paid: Money = Money.ZERO,
                           private var taxes: Money = Money.ZERO,
                           private val movements: MutableList<PatientSourceMovement> = mutableListOf()) {

    fun paid() = paid

    fun taxes() = taxes

    fun movements() = movements.toList()

    fun addSessionCompleted(date: Instant, tax: Money, treatment: String, sessionNumber: Int) =
            SessionCompleted(nextMovement(), date, tax, treatment, sessionNumber).also {
                taxes += tax
                movements.add(it)
            }

    private fun nextMovement() = movements.size + 1

}

sealed class PatientSourceMovement

data class SessionCompleted(val number: Int, val date: Instant, val value: Money, val treatment: String, val sessionNumber: Int) : PatientSourceMovement()
data class Payment(val number: Int, val date: Instant, val value: Money) : PatientSourceMovement()



