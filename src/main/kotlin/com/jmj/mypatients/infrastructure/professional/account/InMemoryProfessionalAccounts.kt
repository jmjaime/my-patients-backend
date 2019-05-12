package com.jmj.mypatients.infrastructure.professional.account

import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccount
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccounts

class InMemoryProfessionalAccounts(private val accounts: MutableMap<String, ProfessionalAccount> = mutableMapOf()) : ProfessionalAccounts {

    override fun findByProfessional(professionalId: String) = accounts[professionalId]

    override fun save(account: ProfessionalAccount) {
        accounts[account.professionalId] = account
    }
}