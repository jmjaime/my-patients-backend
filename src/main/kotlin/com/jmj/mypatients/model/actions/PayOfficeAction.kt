package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.account.MoneyOperation
import com.jmj.mypatients.model.professional.account.ProfessionalAccountService
import java.math.BigDecimal
import java.time.Instant

class PayOfficeAction(private val professionalAccountService: ProfessionalAccountService,
                      private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(payOfficeRequest: PayOfficeRequest) {
        with(payOfficeRequest) {
            val professional = professionalFinder.findProfessionalById(professionalId)
            val office = professionalFinder.findOfficeByProfessionalAndId(professional, officeId)
            professionalAccountService.registerOfficePayment(professional, office, MoneyOperation(Money(value), date))
        }
    }
}

data class PayOfficeRequest(val professionalId: String,
                            val officeId: String,
                            val value: BigDecimal,
                            val date: Instant)