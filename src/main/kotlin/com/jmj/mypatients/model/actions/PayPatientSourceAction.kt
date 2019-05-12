package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.account.MoneyOperation
import com.jmj.mypatients.model.professional.account.ProfessionalAccountService
import java.math.BigDecimal
import java.time.Instant

class PayPatientSourceAction(private val professionalAccountService: ProfessionalAccountService,
                             private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(payPatientSourceRequest: PayPatientSourceRequest) {
        with(payPatientSourceRequest) {
            val professional = professionalFinder.findProfessionalById(professionalId)
            val patientSource = professionalFinder.findPatientSourceByProfessionalAndId(professional, patientSourceId)
            professionalAccountService.registerPatientSourcePayment(professional, patientSource, MoneyOperation(Money(value), date))
        }
    }

}

data class PayPatientSourceRequest(val professionalId: String,
                                   val patientSourceId: String,
                                   val value: BigDecimal,
                                   val date: Instant)