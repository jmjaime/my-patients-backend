package com.jmj.mypatients.domain.professional.account.professional

interface ProfessionalAccounts {

    fun findByProfessional(professionalId: String): ProfessionalAccount?
    fun save(account: ProfessionalAccount)

}