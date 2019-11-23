package com.jmj.mypatients.actions

import com.jmj.mypatients.actions.models.PaymentModel
import com.jmj.mypatients.actions.models.toPayment
import com.jmj.mypatients.domain.money.Money
import com.jmj.mypatients.domain.professional.ProfessionalFinder
import com.jmj.mypatients.domain.professional.account.MoneyOperation
import com.jmj.mypatients.domain.professional.account.ProfessionalAccountService
import java.math.BigDecimal
import java.time.Instant

class PaySessionsAction(private val professionalAccountService: ProfessionalAccountService,
                        private val professionalFinder: ProfessionalFinder) {

    operator fun invoke(paySessionsRequest: PaySessionsRequest): PaymentModel {
        with(paySessionsRequest) {
            val professional = professionalFinder.findProfessionalById(professionalId)
            return professionalAccountService.registerSessionsPayment(professional, treatmentId, sessions, MoneyOperation(Money(value), date)).toPayment()
        }
    }
}

data class PaySessionsRequest(val professionalId: String,
                              val treatmentId: String,
                              val sessions: Set<Int>,
                              val value: BigDecimal,
                              val date: Instant) {
    init {
        check(sessions.isNotEmpty()) { "Al least one session shoul be included" }
        check(BigDecimal.ZERO.compareTo(value) == -1) { "Value should be greater than zero" }
    }
}