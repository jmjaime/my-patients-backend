package com.jmj.mypatients

import com.jmj.mypatients.model.events.EventPublisher
import com.jmj.mypatients.model.events.EventSubscriber
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.ProfessionalInitializer
import com.jmj.mypatients.model.professional.Professionals
import com.jmj.mypatients.model.professional.account.ProfessionalAccountService
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccounts
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Offices
import com.jmj.mypatients.model.treatment.TreatmentService
import com.jmj.mypatients.model.treatment.Treatments
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class MyPatientsConfig {

    @Bean
    fun treatmentService(treatments: Treatments, eventPublisher: EventPublisher, idGenerator: () -> String) =
            TreatmentService(treatments, eventPublisher, idGenerator)

    @Bean
    fun professionalFinder(professionals: Professionals, offices: Offices, patientSources: PatientSources, treatments: Treatments) =
            ProfessionalFinder(professionals, offices, patientSources, treatments)

    @Bean
    fun professionalAccountService(eventSubscriber: EventSubscriber, professionalAccounts: ProfessionalAccounts, officeAccounts: OfficeAccounts,
                                   patientSourceAccounts: PatientSourceAccounts, professionalFinder: ProfessionalFinder,
                                   clock: Clock, idGenerator: () -> String) =
            ProfessionalAccountService(professionalAccounts, officeAccounts, patientSourceAccounts, professionalFinder, clock).apply {
                eventSubscriber.subscribe(this)
            }

    @Bean
    fun professionalInitializer(professionals: Professionals, offices: Offices, patientSources: PatientSources, professionalAccounts: ProfessionalAccounts,
                                officeAccounts: OfficeAccounts, patientSourceAccounts: PatientSourceAccounts, idGenerator: () -> String) =
            ProfessionalInitializer(professionals, offices, patientSources, professionalAccounts, officeAccounts,
                    patientSourceAccounts, idGenerator)

}