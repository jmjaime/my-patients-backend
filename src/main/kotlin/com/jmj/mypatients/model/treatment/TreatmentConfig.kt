package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.model.actions.FindTreatments
import com.jmj.mypatients.model.actions.InitTreatmentAction
import com.jmj.mypatients.model.patient.PatientFinder
import com.jmj.mypatients.model.patient.Patients
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.Professionals
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Offices
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TreatmentConfig {

    @Bean
    fun treatmentService(treatments: Treatments) = TreatmentService(treatments)

    @Bean
    fun patientFinder(patients: Patients) = PatientFinder(patients)

    @Bean
    fun treatmentFinder(treatments: Treatments) = TreatmentFinder(treatments)

    @Bean
    fun professionalFinder(professionals: Professionals, offices: Offices, patientSources: PatientSources, treatments: Treatments) = ProfessionalFinder(professionals, offices, patientSources, treatments)

    @Bean
    fun findTreatmentsAction(professionalFinder: ProfessionalFinder, treatmentFinder: TreatmentFinder) = FindTreatments(professionalFinder, treatmentFinder)

    @Bean
    fun initTreatmentAction(treatmentService: TreatmentService, professionalFinder: ProfessionalFinder, patientFinder: PatientFinder) = InitTreatmentAction(treatmentService, professionalFinder, patientFinder)

}