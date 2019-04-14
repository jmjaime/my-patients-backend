package com.jmj.mypatients

import com.jmj.mypatients.model.actions.AddSessionAction
import com.jmj.mypatients.model.actions.FindProfessionalAction
import com.jmj.mypatients.model.actions.FindTreatmentsAction
import com.jmj.mypatients.model.actions.InitTreatmentAction
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.Professionals
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Offices
import com.jmj.mypatients.model.treatment.TreatmentService
import com.jmj.mypatients.model.treatment.Treatments
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class MyPatientsConfig {

    @Bean
    fun treatmentService(treatments: Treatments) = TreatmentService(treatments) { UUID.randomUUID().toString() }

    @Bean
    fun professionalFinder(professionals: Professionals, offices: Offices, patientSources: PatientSources, treatments: Treatments) = ProfessionalFinder(professionals, offices, patientSources, treatments)

    @Bean
    fun findTreatmentsAction(professionalFinder: ProfessionalFinder) = FindTreatmentsAction(professionalFinder)

    @Bean
    fun initTreatmentAction(treatmentService: TreatmentService, professionalFinder: ProfessionalFinder) = InitTreatmentAction(treatmentService, professionalFinder)

    @Bean
    fun addSessionAction(treatmentService: TreatmentService, professionalFinder: ProfessionalFinder) = AddSessionAction(treatmentService, professionalFinder)

    @Bean
    fun findProfessionalAction(professionalFinder: ProfessionalFinder) = FindProfessionalAction(professionalFinder)

}