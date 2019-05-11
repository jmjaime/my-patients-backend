package com.jmj.mypatients.model.actions.configuration

import com.jmj.mypatients.model.actions.*
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ActionsConfiguration {

    @Bean
    fun findTreatmentsAction(professionalFinder: ProfessionalFinder) = FindTreatmentsAction(professionalFinder)

    @Bean
    fun initTreatmentAction(treatmentService: TreatmentService, professionalFinder: ProfessionalFinder) = InitTreatmentAction(treatmentService, professionalFinder)

    @Bean
    fun addSessionAction(treatmentService: TreatmentService, professionalFinder: ProfessionalFinder) = AddSessionAction(treatmentService, professionalFinder)

    @Bean
    fun findProfessionalAction(professionalFinder: ProfessionalFinder) = FindProfessionalAction(professionalFinder)

    @Bean
    fun findOfficesAction(professionalFinder: ProfessionalFinder) = FindOfficesAction(professionalFinder)

    @Bean
    fun findPatientSourcesAction(professionalFinder: ProfessionalFinder) = FindPatientSourcesAction(professionalFinder)

}