package com.jmj.mypatients.domain.professional.account.professional

import com.jmj.mypatients.domain.professional.account.Account
import com.jmj.mypatients.domain.professional.account.MoneyOperation

data class ProfessionalAccount(val professionalId: String,
                               private val account: Account) {

    fun credit() = account.credit()

    fun debit() = account.debit()

    fun balance() = account.balance()

    fun movements() = account.movements()

    fun addCredit(moneyOperation: MoneyOperation) = account.addCredit(moneyOperation)

    fun addDebit(moneyOperation: MoneyOperation) = account.addDebit(moneyOperation)


}