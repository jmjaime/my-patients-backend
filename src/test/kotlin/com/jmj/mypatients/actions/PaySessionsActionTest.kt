package com.jmj.mypatients.actions

import com.jmj.mypatients.*
import com.jmj.mypatients.actions.models.PaymentModel
import com.jmj.mypatients.domain.errors.ObjectNotFoundException
import com.jmj.mypatients.domain.professional.ProfessionalFinder
import com.jmj.mypatients.domain.professional.account.ProfessionalAccountService
import com.jmj.mypatients.domain.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.domain.professional.account.office.OfficeAccounts
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccounts
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant

class PaySessionsActionTest {

    companion object {
        private val pay = BigDecimal.TEN
        private val now = Instant.now()
        private val sessions = setOf(1,2)
    }

    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var patientSourceAccounts: PatientSourceAccounts
    private lateinit var officeAccounts: OfficeAccounts
    private lateinit var professionalAccounts: ProfessionalAccounts
    private lateinit var professionalAccountService: ProfessionalAccountService
    private lateinit var paySessionsAction: PaySessionsAction

    @Before
    fun setUp() {
        patientSourceAccounts = createPatientSourceAccounts()
        officeAccounts = createOfficeAccounts()
        professionalAccounts = createProfessionalAccounts()
        val treatment = defaultTreatment(sessions.map { defaultSession(it) })
        val treatments = createTreatments(listOf(treatment))
        professionalFinder = ProfessionalFinder(createProfessionals(), createOffices(), createPatientSources(), treatments)
        professionalAccountService = ProfessionalAccountService(professionalAccounts, officeAccounts, patientSourceAccounts, professionalFinder, Clock.systemUTC())
        paySessionsAction = PaySessionsAction(professionalAccountService, professionalFinder)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `pay sessions with invalid treatment`() {
        val request = givenASessionsPayment(treatment = notExists)
        paySessionsAction(request)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `pay sessions with invalid professional`() {
        val request = givenASessionsPayment(professional = notExists)
        paySessionsAction(request)
    }

    @Test(expected = IllegalStateException::class)
    fun `pay sessions with empty sessions list`() {
        val request = givenASessionsPayment(sessionsToPay = emptySet())
        paySessionsAction(request)
    }

    @Test
    fun `pay sessions successfully`() {
        val request = givenASessionsPayment()
        val payment = paySessionsAction(request)
        thenPaymentWasRegistered(payment)
    }

    private fun thenPaymentWasRegistered(payment: PaymentModel) {
        Assertions.assertThat(payment.amount).isEqualTo(pay)
        Assertions.assertThat(payment.date).isEqualTo(now)
    }

    private fun givenASessionsPayment(professional: String = professionalId, treatment: String = treatmentId, sessionsToPay: Set<Int> = sessions) = PaySessionsRequest(professional, treatment, sessionsToPay, pay, now)
}

