package com.jmj.mypatients.model.professional.account.professional

interface ProfessionalAccounts {

    fun findByProfessional(professionalId: String): ProfessionalAccount?
    fun save(account: ProfessionalAccount)

}