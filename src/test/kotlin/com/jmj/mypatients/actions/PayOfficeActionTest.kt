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

class PayOfficeActionTest {

    companion object {
        private val pay = BigDecimal.TEN
        private val now = Instant.now()
    }

    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var patientSourceAccounts: PatientSourceAccounts
    private lateinit var officeAccounts: OfficeAccounts
    private lateinit var professionalAccounts: ProfessionalAccounts
    private lateinit var professionalAccountService: ProfessionalAccountService
    private lateinit var payOfficeAction: PayOfficeAction

    @Before
    fun setUp() {
        patientSourceAccounts = createPatientSourceAccounts()
        officeAccounts = createOfficeAccounts()
        professionalAccounts = createProfessionalAccounts()
        professionalFinder = ProfessionalFinder(createProfessionals(), createOffices(), createPatientSources(), createTreatments())
        professionalAccountService = ProfessionalAccountService(professionalAccounts, officeAccounts, patientSourceAccounts, professionalFinder, Clock.systemUTC())
        payOfficeAction = PayOfficeAction(professionalAccountService, professionalFinder)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `pay to office with invalid office`() {
        val request = givenAnOfficePayment(office = notExists)
        payOfficeAction(request)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `pay to office with invalid professional`() {
        val request = givenAnOfficePayment(professional = notExists)
        payOfficeAction(request)
    }

    @Test
    fun `pay to patient source`() {
        val request = givenAnOfficePayment()
        val payment = payOfficeAction(request)
        thenPaymentWasRegistered(payment)
    }

    private fun thenPaymentWasRegistered(payment: PaymentModel) {
        Assertions.assertThat(payment.amount).isEqualTo(pay)
        Assertions.assertThat(payment.date).isEqualTo(now)
    }

    private fun givenAnOfficePayment(professional: String = professionalId, office: String = officeId) = PayOfficeRequest(professional, office, pay, now)
}

