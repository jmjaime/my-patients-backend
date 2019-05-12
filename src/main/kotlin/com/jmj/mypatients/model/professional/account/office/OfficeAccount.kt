package com.jmj.mypatients.model.professional.account.office

import com.jmj.mypatients.model.professional.account.Account
import com.jmj.mypatients.model.professional.account.MoneyOperation

class OfficeAccount(val id: String,
                    val professionalId: String,
                    val officeId: String,
                    private val account: Account) {

    fun paid() = account.debit()

    fun used() = account.credit()

    fun balance() = account.balance()

    fun movements() = account.movements()

    fun pay(moneyOperation: MoneyOperation) = account.addDebit(moneyOperation)

    fun addUse(moneyOperation: MoneyOperation) = account.addCredit(moneyOperation)

}
