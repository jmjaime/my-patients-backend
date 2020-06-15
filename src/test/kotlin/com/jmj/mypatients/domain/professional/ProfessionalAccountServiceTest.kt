package com.jmj.mypatients.domain.professional

import com.jmj.mypatients.*
import com.jmj.mypatients.domain.money.Money
import com.jmj.mypatients.domain.professional.account.MoneyOperation
import com.jmj.mypatients.domain.professional.account.ProfessionalAccountService
import com.jmj.mypatients.domain.professional.account.derivation.PatientSourceAccount
import com.jmj.mypatients.domain.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.domain.professional.account.office.OfficeAccount
import com.jmj.mypatients.domain.professional.account.office.OfficeAccounts
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccount
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccounts
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.time.Clock
import java.time.Instant

class ProfessionalAccountServiceTest {

    private val professional = defaultProfessional()
    private val office = defaultOffice()
    private val patientSource = defaultPatientSource()
    private val treatment = defaultTreatment()
    private val sessionNumber = 1
    private val now = Instant.now()

    private lateinit var clock: Clock
    private lateinit var professionalAccount: ProfessionalAccount
    private lateinit var officeAccount: OfficeAccount
    private lateinit var patientSourceAccount: PatientSourceAccount
    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var patientSourceAccounts: PatientSourceAccounts
    private lateinit var officeAccounts: OfficeAccounts
    private lateinit var professionalAccounts: ProfessionalAccounts
    private lateinit var professionalAccountService: ProfessionalAccountService

    @Before
    fun setUp() {
        professionalAccount = defaultProfessionalAccount()
        officeAccount = defaultOfficeAccount()
        patientSourceAccount = defaultPatientSourceAccount()
        professionalFinder = mockProfessionalFinder()
        officeAccounts = mockOfficeAccounts()
        patientSourceAccounts = mockPatientSourceAccounts()
        professionalAccounts = mockAccounts()
        clock = mockClock()
        professionalAccountService = ProfessionalAccountService(professionalAccounts, officeAccounts, patientSourceAccounts, professionalFinder, clock)
    }

    @Test
    fun `impact accounts with new not paid session`() {
        givenASession(paid = false)
        whenRegisterNewSession()
        thenProfessionalAccountCreditShouldBe(credit = Money.ZERO, debit = Money.ZERO)
        thenOfficeAccountShouldBe(used = office.cost, paid = Money.ZERO)
        thenPatientSourceAccountShouldBe(taxes = treatment.derivation.currentFee, paid = Money.ZERO)
    }

    @Test
    fun `impact accounts with new paid session`() {
        givenASession(paid = true)
        whenRegisterNewSession()
        thenProfessionalAccountCreditShouldBe(credit = treatment.derivation.currentFee, debit = Money.ZERO)
        thenOfficeAccountShouldBe(used = office.cost, paid = Money.ZERO)
        thenPatientSourceAccountShouldBe(taxes = treatment.derivation.currentFee, paid = Money.ZERO)
    }

    @Test
    fun `impact on office account with an office payment`() {
        val used = Money(100)
        val payment = Money(50)
        givenAnOfficeWithSomeUse(used)
        whenRegisterAnOfficePayment(payment)
        thenOfficeAccountShouldBe(used = used, paid = payment)
    }

    @Test
    fun `impact on patient source account with an patient source payment`() {
        val taxes = Money(100)
        val payment = Money(50)
        givenAPatientSourceWithSomeTaxes(taxes)
        whenRegisterAPatientSourcePayment(payment)
        thenPatientSourceAccountShouldBe(taxes = taxes, paid = payment)
    }

    private fun givenAPatientSourceWithSomeTaxes(taxes: Money) {
        patientSourceAccount.addTax(MoneyOperation(taxes, now))
    }

    private fun givenAnOfficeWithSomeUse(used: Money) {
        officeAccount.addUse(MoneyOperation(used, now))
    }

    private fun givenASession(paid: Boolean) {
        treatment.addSession(clock.instant(), office.id, treatment.derivation.currentFee, paid)
    }

    private fun whenRegisterAPatientSourcePayment(payment: Money) {
        professionalAccountService.registerPatientSourcePayment(professional, patientSource, MoneyOperation(payment, now))
    }

    private fun whenRegisterNewSession() {
        professionalAccountService.registerNewSession(professional.id, treatment.id, sessionNumber)
    }

    private fun whenRegisterAnOfficePayment(payment: Money) {
        professionalAccountService.registerOfficePayment(professional, office, MoneyOperation(payment, now))
    }

    private fun thenPatientSourceAccountShouldBe(taxes: Money, paid: Money) {
        Assertions.assertThat(patientSourceAccount.paid()).isEqualTo(paid)
        Assertions.assertThat(patientSourceAccount.taxes()).isEqualTo(taxes)
        Assertions.assertThat(patientSourceAccount.balance()).isEqualTo(taxes.minus(paid))
    }

    private fun thenOfficeAccountShouldBe(used: Money, paid: Money) {
        Assertions.assertThat(officeAccount.paid()).isEqualTo(paid)
        Assertions.assertThat(officeAccount.used()).isEqualTo(used)
        Assertions.assertThat(officeAccount.balance()).isEqualTo(used.minus(paid))

    }

    private fun thenProfessionalAccountCreditShouldBe(credit: Money, debit: Money) {
        Assertions.assertThat(professionalAccount.credit()).isEqualTo(credit)
        Assertions.assertThat(professionalAccount.debit()).isEqualTo(debit)
        Assertions.assertThat(professionalAccount.balance()).isEqualTo(credit.minus(debit))

    }

    private fun mockProfessionalFinder() = Mockito.mock(ProfessionalFinder::class.java).also {
        Mockito.`when`(it.findProfessionalById(professional.id)).thenReturn(professional)
        Mockito.`when`(it.findOfficeByProfessionalAndId(professional, office.id)).thenReturn(office)
        Mockito.`when`(it.findTreatmentByProfessionalAndId(professional, treatment.id)).thenReturn(treatment)
    }

    private fun mockPatientSourceAccounts() = Mockito.mock(PatientSourceAccounts::class.java).also {
        Mockito.`when`(it.findByProfessionalAndId(professional.id, patientSource.id)).thenReturn(patientSourceAccount)
    }

    private fun mockOfficeAccounts() = Mockito.mock(OfficeAccounts::class.java).also {
        Mockito.`when`(it.findByProfessionalAndId(professional.id, office.id)).thenReturn(officeAccount)
    }

    private fun mockAccounts() = Mockito.mock(ProfessionalAccounts::class.java).also {
        Mockito.`when`(it.findByProfessional(professional.id)).thenReturn(professionalAccount)
    }

    private fun mockClock() = Mockito.mock(Clock::class.java).also {
        Mockito.`when`(it.instant()).thenReturn(now)
    }
}