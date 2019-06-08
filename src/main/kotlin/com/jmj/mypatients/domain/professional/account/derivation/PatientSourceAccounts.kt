package com.jmj.mypatients.domain.professional.account.derivation

interface PatientSourceAccounts {

    fun findByProfessionalAndId(professionalId: String, patientSourceId: String): PatientSourceAccount?
    fun save(patientSourceAccount: PatientSourceAccount)

}