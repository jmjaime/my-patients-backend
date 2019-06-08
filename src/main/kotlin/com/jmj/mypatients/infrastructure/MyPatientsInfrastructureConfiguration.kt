package com.jmj.mypatients.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.jmj.mypatients.domain.professional.ProfessionalInitializer
import com.jmj.mypatients.domain.professional.Professionals
import com.jmj.mypatients.domain.professional.derivation.PatientSources
import com.jmj.mypatients.domain.professional.office.Offices
import com.jmj.mypatients.domain.treatment.Treatments
import com.jmj.mypatients.infrastructure.persistence.professional.InMemoryProfessionals
import com.jmj.mypatients.infrastructure.persistence.treatment.InMemoryTreatments
import com.jmj.mypatients.infrastructure.persistence.treatment.derivation.InMemoryPatientSources
import com.jmj.mypatients.infrastructure.persistence.treatment.office.InMemoryOffices
import com.jmj.mypatients.infrastructure.security.MyPatientUser
import com.jmj.mypatients.infrastructure.security.MyPatientsUserDetailsService
import com.jmj.mypatients.infrastructure.security.Role
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import java.time.Clock

const val myPatientsApiV1BasePath = "/api/v1/my-patients/{professionalId}"

@Configuration
class MyPatientsInfrastructureConfiguration {

    private val ids = SecuenceIdGenerator()

    @Bean
    fun idGenerator(): () -> String = { ids.next().toString() }

    @Bean
    fun treatments(): Treatments = InMemoryTreatments()

    @Bean
    fun offices(): Offices = InMemoryOffices()

    @Bean
    fun professionals(): Professionals = InMemoryProfessionals()

    @Bean
    fun patientSources(): PatientSources = InMemoryPatientSources()

    @Bean
    fun objectMapper(): ObjectMapper =
            ObjectMapper()
                    .registerModule(JavaTimeModule())
                    .registerModule(ParameterNamesModule())
                    .registerModule(Jdk8Module())
                    .registerModule(KotlinModule())

    @Bean
    fun clock(): Clock = Clock.systemDefaultZone()

    @Bean
    fun dataTestInitializer(professionalInitializer: ProfessionalInitializer, userDetailsService: UserDetailsService) =
            ProfessionalDataTestInitializer(professionalInitializer, userDetailsService)
}

class ProfessionalDataTestInitializer(private val professionalInitializer: ProfessionalInitializer,
                                      private val userDetailsService: UserDetailsService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val professionals = professionalInitializer.newProfessional("Jose")
        with(userDetailsService as MyPatientsUserDetailsService) {
            addUser(MyPatientUser("user", "{noop}password",
                    listOf(SimpleGrantedAuthority(Role.PROFESSIONAL.name)), professionals.id))
        }
    }

}

class SecuenceIdGenerator(private var nextId: Int = 0) {
    fun next(): Int {
        nextId += 1
        return nextId
    }
}
