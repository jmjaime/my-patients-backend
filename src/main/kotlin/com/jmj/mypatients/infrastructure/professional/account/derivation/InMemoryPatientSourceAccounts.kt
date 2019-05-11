package com.jmj.mypatients.infrastructure.professional.account.derivation

import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccount
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts

class InMemoryPatientSourceAccounts(private val patientSourceAccounts: MutableMap<String, PatientSourceAccount> = mutableMapOf()) : PatientSourceAccounts {
    override fun findByProfessionalAndId(professionalId: String, patientSourceId: String) =
            patientSourceAccounts.values.firstOrNull { it.patientSourceId == patientSourceId && it.professionalId == professionalId }


    override fun save(patientSourceAccount: PatientSourceAccount) {
        patientSourceAccounts[patientSourceAccount.id]
    }
}