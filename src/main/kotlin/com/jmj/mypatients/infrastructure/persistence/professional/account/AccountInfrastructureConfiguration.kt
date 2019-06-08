package com.jmj.mypatients.infrastructure.persistence.professional.account

import com.jmj.mypatients.domain.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.domain.professional.account.office.OfficeAccounts
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccounts
import com.jmj.mypatients.infrastructure.persistence.professional.account.derivation.InMemoryPatientSourceAccounts
import com.jmj.mypatients.infrastructure.persistence.professional.account.office.InMemoryOfficeAccounts
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
