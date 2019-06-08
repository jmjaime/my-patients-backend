package com.jmj.mypatients.infrastructure.persistence.professional.account

import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccount
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccounts

class InMemoryProfessionalAccounts(private val accounts: MutableMap<String, ProfessionalAccount> = mutableMapOf()) : ProfessionalAccounts {

    override fun findByProfessional(professionalId: String) = accounts[professionalId]

    override fun save(account: ProfessionalAccount) {
        accounts[account.professionalId] = account
    }
}