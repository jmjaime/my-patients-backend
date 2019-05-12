package com.jmj.mypatients.infrastructure.professional.account

import com.jmj.mypatients.infrastructure.professional.account.derivation.InMemoryPatientSourceAccounts
import com.jmj.mypatients.infrastructure.professional.account.office.InMemoryOfficeAccounts
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccounts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountInfrastructureConfiguration {

    @Bean
    fun accounts(): ProfessionalAccounts = InMemoryProfessionalAccounts()

    @Bean
    fun patientSourceAcounts(): PatientSourceAccounts = InMemoryPatientSourceAccounts()

    @Bean
    fun officeAcounts(): OfficeAccounts = InMemoryOfficeAccounts()

}
