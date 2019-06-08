package com.jmj.mypatients.domain.professional

import com.jmj.mypatients.domain.money.Money
import com.jmj.mypatients.domain.professional.account.derivation.PatientSourceAccount
import com.jmj.mypatients.domain.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.domain.professional.account.office.OfficeAccount
import com.jmj.mypatients.domain.professional.account.office.OfficeAccounts
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccount
import com.jmj.mypatients.domain.professional.account.professional.ProfessionalAccounts
import com.jmj.mypatients.domain.professional.derivation.PatientSource
import com.jmj.mypatients.domain.professional.derivation.PatientSources
import com.jmj.mypatients.domain.professional.office.Office
import com.jmj.mypatients.domain.professional.office.Offices
import com.jmj.mypatients.professionalId
import com.jmj.mypatients.professionalName
import com.nhaarman.mockito_kotlin.capture
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.internal.verification.Times

class ProfessionalInitializerTest {

    @Captor
    private lateinit var officeCapture: ArgumentCaptor<Office>
    @Captor
    private lateinit var patientSourceCapture: ArgumentCaptor<PatientSource>
    @Captor
    private lateinit var professionalAccountCapture: ArgumentCaptor<ProfessionalAccount>
    @Captor
    private lateinit var officeAccountCapture: ArgumentCaptor<OfficeAccount>
    @Captor
    private lateinit var patientSourceAccountCapture: ArgumentCaptor<PatientSourceAccount>
    @Mock
    private lateinit var professionals: Professionals
    @Mock
    private lateinit var offices: Offices
    @Mock
    private lateinit var patientSources: PatientSources
    @Mock
    private lateinit var professionalAccounts: ProfessionalAccounts
    @Mock
    private lateinit var officeAccounts: OfficeAccounts
    @Mock
    private lateinit var patientSourceAccounts: PatientSourceAccounts

    private lateinit var professionalInitializer: ProfessionalInitializer


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        professionalInitializer = ProfessionalInitializer(professionals, offices, patientSources, professionalAccounts, officeAccounts, patientSourceAccounts) { professionalId }
    }

    @Test
    fun `create professional init default office and patient source`() {
        val professional = professionalInitializer.newProfessional(professionalName)
        assertProfessional(professional)
        assertOfficeCreated(professional)
        assertPatientSourceCreated(professional)
    }

    private fun assertProfessional(professional: Professional) {
        Assertions.assertThat(professional.name).isEqualTo(professionalName)
        Assertions.assertThat(professional.id).isEqualTo(professionalId)
        assertProfessionalAccount(professional)
    }

    private fun assertProfessionalAccount(professional: Professional) {
        Mockito.verify(professionalAccounts, Times(1)).save(capture(professionalAccountCapture))
        val account = professionalAccountCapture.value
        Assertions.assertThat(account.professionalId).isEqualTo(professional.id)
        Assertions.assertThat(account.credit()).isEqualTo(Money.ZERO)
        Assertions.assertThat(account.debit()).isEqualTo(Money.ZERO)
        Assertions.assertThat(account.movements()).isEmpty()
    }

    private fun assertOfficeCreated(professional: Professional) {
        Mockito.verify(offices, Times(1)).save(capture(officeCapture))
        val defaultOffice = officeCapture.value
        Assertions.assertThat(defaultOffice.cost).isEqualTo(Money.ZERO)
        Assertions.assertThat(defaultOffice.professionalId).isEqualTo(professional.id)
        Assertions.assertThat(defaultOffice.name).isEqualTo(Office.DEFUALT_OFFICE)
        assertOfficeAccount(professional, defaultOffice)
    }

    private fun assertOfficeAccount(professional: Professional, defaultOffice: Office) {
        Mockito.verify(officeAccounts, Times(1)).save(capture(officeAccountCapture))
        val officeAccount = officeAccountCapture.value
        Assertions.assertThat(officeAccount.professionalId).isEqualTo(professional.id)
        Assertions.assertThat(officeAccount.officeId).isEqualTo(defaultOffice.id)
        Assertions.assertThat(officeAccount.paid()).isEqualTo(Money.ZERO)
        Assertions.assertThat(officeAccount.used()).isEqualTo(Money.ZERO)
        Assertions.assertThat(officeAccount.movements()).isEmpty()
    }

    private fun assertPatientSourceCreated(professional: Professional) {
        Mockito.verify(patientSources, Times(1)).save(capture(patientSourceCapture))
        val patientSourceParticular = patientSourceCapture.value
        Assertions.assertThat(patientSourceParticular.fee).isEqualTo(Money.ZERO)
        Assertions.assertThat(patientSourceParticular.professionalId).isEqualTo(professional.id)
        Assertions.assertThat(patientSourceParticular.source).isEqualTo(PatientSource.PARTICULAR)
        assertPatientSourceAccount(professional, patientSourceParticular)
    }

    private fun assertPatientSourceAccount(professional: Professional, patientSource: PatientSource) {
        Mockito.verify(patientSourceAccounts, Times(1)).save(capture(patientSourceAccountCapture))
        val patientSourceAccount = patientSourceAccountCapture.value
        Assertions.assertThat(patientSourceAccount.professionalId).isEqualTo(professional.id)
        Assertions.assertThat(patientSourceAccount.patientSourceId).isEqualTo(patientSource.id)
        Assertions.assertThat(patientSourceAccount.paid()).isEqualTo(Money.ZERO)
        Assertions.assertThat(patientSourceAccount.taxes()).isEqualTo(Money.ZERO)
        Assertions.assertThat(patientSourceAccount.movements()).isEmpty()
    }


}
