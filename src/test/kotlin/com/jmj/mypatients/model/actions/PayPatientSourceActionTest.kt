package com.jmj.mypatients.model.actions

import com.jmj.mypatients.*
import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.account.ProfessionalAccountService
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccounts
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
        payPatientSourceAction(request)
        thenPaymentWasRegistered()
    }

    private fun thenPaymentWasRegistered() {
        val patientSourceAccount = patientSourceAccounts.findByProfessionalAndId(professionalId, patientSourceId)
        with(patientSourceAccount!!) {
            Assertions.assertThat(this.paid()).isEqualTo(Money(pay))
        }
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

    private fun givenAnPatientSourcePayment(professional: String = professionalId, patientSource: String = patientSourceId) =
            PayPatientSourceRequest(professional, patientSource, pay, now)
}