package com.jmj.mypatients.model.professional.account

interface Accounts {

    fun findByProfessional(professionalId: String): Account?
    fun save(account: Account)

}