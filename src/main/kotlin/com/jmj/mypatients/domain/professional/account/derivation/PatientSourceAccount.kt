package com.jmj.mypatients.domain.professional.account.derivation

import com.jmj.mypatients.domain.professional.account.Account
import com.jmj.mypatients.domain.professional.account.MoneyOperation

class PatientSourceAccount(val id: String,
                           val professionalId: String,
                           val patientSourceId: String,
                           private val account: Account) {

    fun paid() = account.debit()

    fun taxes() = account.credit()

    fun balance() = account.balance()

    fun movements() = account.movements()

    fun pay(moneyOperation: MoneyOperation) = account.addDebit(moneyOperation)

    fun addTax(moneyOperation: MoneyOperation) = account.addCredit(moneyOperation)
}



