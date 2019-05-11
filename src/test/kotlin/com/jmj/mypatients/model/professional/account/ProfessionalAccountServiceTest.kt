package com.jmj.mypatients.model.professional.account

import com.jmj.mypatients.*
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccount
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.model.professional.account.derivation.SessionCompleted
import com.jmj.mypatients.model.professional.account.office.OfficeAccount
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts
import com.jmj.mypatients.model.professional.account.office.Use
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.time.Clock

class ProfessionalAccountServiceTest {

    private val professional = defaultProfessional()
    private val office = defaultOffice()
    private val patientSource = defaultPatientSource()
    private val treatment = defaultTreatment()
    private val sessionNumber = 1
    private val now = Clock.systemDefaultZone().instant()

    private lateinit var clock: Clock
    private lateinit var account: Account
    private lateinit var officeAccount: OfficeAccount
    private lateinit var patientSourceAccount: PatientSourceAccount
    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var patientSourceAccounts: PatientSourceAccounts
    private lateinit var officeAccounts: OfficeAccounts
    private lateinit var accounts: Accounts
    private lateinit var professionalAccountService: ProfessionalAccountService

    @Before
    fun setUp() {
        account = defaultAccount()
        officeAccount = defaultOfficeAccount()
        patientSourceAccount = defaultPatientSourceAccount()
        professionalFinder = mockProfessionalFinder()
        officeAccounts = mockOfficeAccounts()
        patientSourceAccounts = mockPatientSourceAccounts()
        accounts = mockAccounts()
        clock = mockClock()
        professionalAccountService = ProfessionalAccountService(accounts, officeAccounts, patientSourceAccounts, professionalFinder, clock)
    }

    @Test
    fun `impact accounts with new not paid session`() {
        givenASession(paid = false)
        whenRegisterNewSession()
        thenShouldNotAddCreditToProfessionalAccount()
        thenShouldAddPendingToOfficeAccount()
        thenShouldAddPendingToPatientSourceAccount()
    }

    @Test
    fun `impact accounts with new paid session`() {
        givenASession(paid = true)
        whenRegisterNewSession()
        thenShouldAddCreditToProfessionalAccount()
        thenShouldAddPendingToOfficeAccount()
        thenShouldAddPendingToPatientSourceAccount()
    }

    private fun thenShouldAddPendingToPatientSourceAccount() {
        Assertions.assertThat(patientSourceAccount.paid()).isEqualTo(Money.ZERO)
        Assertions.assertThat(patientSourceAccount.taxes()).isEqualTo(office.cost)
        Assertions.assertThat(patientSourceAccount.movements().size).isEqualTo(1)
        when (val movement = patientSourceAccount.movements().first()) {
            is SessionCompleted -> {
                Assertions.assertThat(movement.date).isEqualTo(now)
                Assertions.assertThat(movement.number).isEqualTo(1)
                Assertions.assertThat(movement.sessionNumber).isEqualTo(1)
                Assertions.assertThat(movement.treatment).isEqualTo(treatment.id)
            }
            else -> Assert.fail("invalid movement")
        }
    }

    private fun thenShouldAddPendingToOfficeAccount() {
        Assertions.assertThat(officeAccount.paid()).isEqualTo(Money.ZERO)
        Assertions.assertThat(officeAccount.used()).isEqualTo(office.cost)
        Assertions.assertThat(officeAccount.movements().size).isEqualTo(1)
        when (val movement = officeAccount.movements().first()) {
            is Use -> {
                Assertions.assertThat(movement.date).isEqualTo(now)
                Assertions.assertThat(movement.number).isEqualTo(1)
                Assertions.assertThat(movement.sessionNumber).isEqualTo(1)
                Assertions.assertThat(movement.treatment).isEqualTo(treatment.id)
            }
            else -> Assert.fail("invalid movement")
        }
    }

    private fun thenShouldNotAddCreditToProfessionalAccount() {
        Assertions.assertThat(account.credit()).isEqualTo(Money.ZERO)
        Assertions.assertThat(account.movements()).isEmpty()
    }

    private fun thenShouldAddCreditToProfessionalAccount() {
        Assertions.assertThat(account.credit()).isEqualTo(treatment.derivation.currentFee)
        Assertions.assertThat(account.movements().size).isEqualTo(1)
        val movement = account.movements().first()
        Assertions.assertThat(movement.date).isEqualTo(now)
        Assertions.assertThat(movement.number).isEqualTo(1)
        Assertions.assertThat(movement.value).isEqualTo(treatment.derivation.currentFee)
        Assertions.assertThat(movement.movementType).isEqualTo(MovementType.CREDIT)
    }

    private fun whenRegisterNewSession() {
        professionalAccountService.registerNewSession(professional.id, treatment.id, sessionNumber)
    }

    private fun givenASession(paid: Boolean) {
        treatment.addSession(clock.instant(), office.id, treatment.derivation.currentFee, paid)
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

    private fun mockAccounts() = Mockito.mock(Accounts::class.java).also {
        Mockito.`when`(it.findByProfessional(professional.id)).thenReturn(account)
    }

    private fun mockClock() = Mockito.mock(Clock::class.java).also {
        Mockito.`when`(it.instant()).thenReturn(now)
    }
}