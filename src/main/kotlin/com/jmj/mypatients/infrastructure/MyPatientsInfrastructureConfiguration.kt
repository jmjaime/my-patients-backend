package com.jmj.mypatients.infrastructure

import com.jmj.mypatients.infrastructure.professional.InMemoryProfessionals
import com.jmj.mypatients.infrastructure.treatment.derivation.InMemoryPatientSources
import com.jmj.mypatients.infrastructure.treatment.office.InMemoryOffices
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.Professionals
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices
import com.jmj.mypatients.model.treatment.Treatments
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.jmj.mypatients.infrastructure.treatment.InMemoryTreatments
import com.jmj.mypatients.model.professional.Professional

const val myPatientsApiV1BasePath = "/api/v1/my-patients/{professionalId}"

@Configuration
class MyPatientsInfrastructureConfiguration {

    private val patientSourceId = "4"
    private val officeId = "6"
    private val professionalId = "1"

    @Bean
    fun treatments(): Treatments = InMemoryTreatments()

    @Bean
    fun offices(): Offices = InMemoryOffices(mutableMapOf(Pair(officeId, Office(officeId, description = "Office", professionalId = professionalId))))

    @Bean
    fun professionals(): Professionals = InMemoryProfessionals(mutableMapOf(Pair(professionalId, Professional(professionalId, "Professional"))))

    @Bean
    fun patientSources(): PatientSources = InMemoryPatientSources(mutableMapOf(Pair(patientSourceId, PatientSource(id = patientSourceId, source = "Particular", fee = Money(0), tax = BigDecimal.ZERO, professionalId = professionalId))))

    @Bean
    fun objectMapper(): ObjectMapper =
            ObjectMapper()
                    .registerModule(JavaTimeModule())
                    .registerModule(ParameterNamesModule())
                    .registerModule(Jdk8Module())
                    .registerModule(KotlinModule())
}
