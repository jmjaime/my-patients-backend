package com.jmj.mypatients.infrastructure.professional.account

import com.jmj.mypatients.model.professional.account.Account
import com.jmj.mypatients.model.professional.account.Accounts

class InMemoryAccounts(private val accounts: MutableMap<String, Account> = mutableMapOf()) : Accounts {

    override fun findByProfessional(professionalId: String) = accounts[professionalId]

    override fun save(account: Account) {
        accounts[account.professionalId] = account
    }
}