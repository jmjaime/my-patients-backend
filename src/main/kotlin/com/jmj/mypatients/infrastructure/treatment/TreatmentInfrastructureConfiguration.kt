package com.jmj.mypatients.infrastructure.treatment

import com.jmj.mypatients.infrastructure.patient.InMemoryPatients
import com.jmj.mypatients.infrastructure.professional.InMemoryProfessionals
import com.jmj.mypatients.infrastructure.treatment.derivation.InMemoryPatientSources
import com.jmj.mypatients.infrastructure.treatment.office.InMemoryOffices
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.patient.Patients
import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.Professionals
import com.jmj.mypatients.model.treatment.Treatments
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class TreatmentInfrastructureConfiguration {

    @Bean
    fun treatments(): Treatments = InMemoryTreatments()

    @Bean
    fun patients() : Patients = InMemoryPatients(mutableMapOf(Pair(1L, Patient(id = 1L, name = "patient", professionalId = 1L))))

    @Bean
    fun offices(): Offices = InMemoryOffices(mutableMapOf(Pair(1L, Office(id = 1L, name = "Office", professionalId = 1L))))

    @Bean
    fun professionals(): Professionals = InMemoryProfessionals( mutableMapOf(Pair(1L, Professional(1L,"Professional"))))

    @Bean
    fun patientSources(): PatientSources = InMemoryPatientSources(mutableMapOf(Pair(1L, PatientSource(id = 1L, name = "Particular", fee = Money(0), tax = 0.toDouble(), professionalId = 1L))))
}