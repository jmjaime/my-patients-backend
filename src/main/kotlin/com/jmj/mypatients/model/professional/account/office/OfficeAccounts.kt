package com.jmj.mypatients.model.professional.account.office

interface OfficeAccounts {

    fun findByProfessionalAndId(professionalId: String, officeId: String): OfficeAccount?
    fun save(officeAccount: OfficeAccount)

}