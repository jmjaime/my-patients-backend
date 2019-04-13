package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService
import java.math.BigDecimal
import java.time.LocalDate

class NewSessionAction(private val treatmentService: TreatmentService, private val professionalFinder: ProfessionalFinder) {

    fun execute(newSessionRequest: NewSessionRequest): SessionModel {
        with(newSessionRequest) {
            val professional = professionalFinder.findById(professionalId)
            val office = professionalFinder.findProfessionalOfficeById(professional, officeId)
            val treatment = professionalFinder.getProfessionalTreatmentById(professional, treatmentId)
            return treatmentService.newSession(treatment, date, office, Money(fee), paid).toModel()
        }
    }

}

data class NewSessionRequest(val professionalId: String, val treatmentId: String, val officeId: String, val patient: String, val date: LocalDate, val fee: BigDecimal, val paid: Boolean)
