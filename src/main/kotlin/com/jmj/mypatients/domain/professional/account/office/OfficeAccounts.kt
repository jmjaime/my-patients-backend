package com.jmj.mypatients.domain.professional.account.office

interface OfficeAccounts {

    fun findByProfessionalAndId(professionalId: String, officeId: String): OfficeAccount?
    fun save(officeAccount: OfficeAccount)

}