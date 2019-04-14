package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService
import java.math.BigDecimal
import java.time.LocalDate

class AddSessionAction(private val treatmentService: TreatmentService, private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(addSessionRequest: AddSessionRequest): SessionModel {
        with(addSessionRequest) {
            val professional = professionalFinder.findById(professionalId)
            val office = professionalFinder.findProfessionalOfficeById(professional, officeId)
            val treatment = professionalFinder.getProfessionalTreatmentById(professional, treatmentId)
            return treatmentService.newSession(treatment, date, office, Money(fee), paid).toModel(treatmentId)
        }
    }

}

data class AddSessionRequest(val professionalId: String, val treatmentId: String, val officeId: String, val date: LocalDate, val fee: BigDecimal, val paid: Boolean)
