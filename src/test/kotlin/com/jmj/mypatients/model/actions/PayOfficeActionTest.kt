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
        payOfficeAction(request)
        thenPaymentWasRegistered()
    }

    private fun thenPaymentWasRegistered() {
        val officeAccount = officeAccounts.findByProfessionalAndId(professionalId, officeId)
        with(officeAccount!!) {
            Assertions.assertThat(this.paid()).isEqualTo(Money(pay))
        }
    }

    private fun givenAnOfficePayment(professional: String = professionalId, office: String = officeId) = PayOfficeRequest(professional, office, pay, now)
}

