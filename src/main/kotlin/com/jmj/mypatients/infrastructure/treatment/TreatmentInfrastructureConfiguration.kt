package com.jmj.mypatients.infrastructure.treatment

import com.jmj.mypatients.infrastructure.professional.InMemoryProfessionals
import com.jmj.mypatients.infrastructure.treatment.derivation.InMemoryPatientSources
import com.jmj.mypatients.infrastructure.treatment.office.InMemoryOffices
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.Professionals
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices
import com.jmj.mypatients.model.treatment.Treatments
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal


@Configuration
class TreatmentInfrastructureConfiguration {

    private val patientSourceId = "4"
    private val officeId = "6"
    private val professionalId = "1"

    @Bean
    fun treatments(): Treatments = InMemoryTreatments()

    @Bean
    fun offices(): Offices = InMemoryOffices(mutableMapOf(Pair(officeId, Office(officeId, name = "Office", professionalId = professionalId))))

    @Bean
    fun professionals(): Professionals = InMemoryProfessionals(mutableMapOf(Pair(professionalId, Professional(professionalId, "Professional"))))

    @Bean
    fun patientSources(): PatientSources = InMemoryPatientSources(mutableMapOf(Pair(patientSourceId, PatientSource(id = patientSourceId, name = "Particular", fee = Money(0), tax = BigDecimal.ZERO, professionalId = professionalId))))

}
