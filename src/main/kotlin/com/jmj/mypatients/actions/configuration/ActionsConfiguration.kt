package com.jmj.mypatients.actions.configuration

import com.jmj.mypatients.actions.*
import com.jmj.mypatients.domain.professional.ProfessionalFinder
import com.jmj.mypatients.domain.professional.account.ProfessionalAccountService
import com.jmj.mypatients.domain.treatment.TreatmentService
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

    @Bean
    fun payOfficeAction(professionalAccountService: ProfessionalAccountService, professionalFinder: ProfessionalFinder) = PayOfficeAction(professionalAccountService, professionalFinder)

    @Bean
    fun payPatientSourceAction(professionalAccountService: ProfessionalAccountService, professionalFinder: ProfessionalFinder) = PayPatientSourceAction(professionalAccountService, professionalFinder)

}