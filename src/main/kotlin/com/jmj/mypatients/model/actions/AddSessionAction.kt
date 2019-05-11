package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.actions.models.SessionModel
import com.jmj.mypatients.model.actions.models.toModel
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService
import java.math.BigDecimal
import java.time.Instant

class AddSessionAction(private val treatmentService: TreatmentService, private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(addSessionRequest: AddSessionRequest): SessionModel {
        with(addSessionRequest) {
            val professional = professionalFinder.findProfessionalById(professionalId)
            val office = professionalFinder.findOfficeByProfessionalAndId(professional, officeId)
            val treatment = professionalFinder.findTreatmentByProfessionalAndId(professional, treatmentId)
            return treatmentService.newSession(treatment, date, office, Money(fee), paid).toModel(treatmentId)
        }
    }

}

data class AddSessionRequest(val professionalId: String,
                             val treatmentId: String,
                             val officeId: String,
                             val date: Instant,
                             val fee: BigDecimal,
                             val paid: Boolean)
