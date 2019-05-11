package com.jmj.mypatients.infrastructure.professional.account.office

import com.jmj.mypatients.model.professional.account.office.OfficeAccount
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts

class InMemoryOfficeAccounts(private val officeAccounts: MutableMap<String, OfficeAccount> = mutableMapOf()) : OfficeAccounts {

    override fun findByProfessionalAndId(professionalId: String, officeId: String) =
            officeAccounts.values.firstOrNull { it.officeId == officeId && it.professionalId == professionalId }

    override fun save(officeAccount: OfficeAccount) {
        officeAccounts[officeAccount.id] = officeAccount
    }

}