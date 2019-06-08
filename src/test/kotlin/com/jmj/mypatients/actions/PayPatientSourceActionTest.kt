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

class PayPatientSourceActionTest {

    companion object {
        private val pay = BigDecimal.TEN
        private val now = Instant.now()
    }

    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var patientSourceAccounts: PatientSourceAccounts
    private lateinit var officeAccounts: OfficeAccounts
    private lateinit var professionalAccounts: ProfessionalAccounts
    private lateinit var professionalAccountService: ProfessionalAccountService
    private lateinit var payPatientSourceAction: PayPatientSourceAction

    @Before
    fun setUp() {
        patientSourceAccounts = createPatientSourceAccounts()
        officeAccounts = createOfficeAccounts()
        professionalAccounts = createProfessionalAccounts()
        professionalFinder = ProfessionalFinder(createProfessionals(), createOffices(), createPatientSources(), createTreatments())
        professionalAccountService = ProfessionalAccountService(professionalAccounts, officeAccounts, patientSourceAccounts, professionalFinder, Clock.systemUTC())
        payPatientSourceAction = PayPatientSourceAction(professionalAccountService, professionalFinder)
    }

    @Test
    fun `pay to patient source`() {
        val request = givenAnPatientSourcePayment()
        val payment = payPatientSourceAction(request)
        thenPaymentWasRegistered(payment)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `pay to patient source with invalid office`() {
        val request = givenAnPatientSourcePayment(patientSource = notExists)
        payPatientSourceAction(request)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `pay to patient source with invalid professional`() {
        val request = givenAnPatientSourcePayment(professional = notExists)
        payPatientSourceAction(request)
    }

    private fun thenPaymentWasRegistered(payment: PaymentModel) {
        Assertions.assertThat(payment.date).isEqualTo(now)
        Assertions.assertThat(payment.amount).isEqualTo(pay)

    }

    private fun givenAnPatientSourcePayment(professional: String = professionalId, patientSource: String = patientSourceId) =
            PayPatientSourceRequest(professional, patientSource, pay, now)
}