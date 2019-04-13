package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.*
import com.jmj.mypatients.model.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.model.treatment.session.Session
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.time.LocalDate

class TreatmentServiceTest {

    private val date = LocalDate.now()
    private val notPaid = false
    private val professional = defaultProfessional()
    private val patientSource = defaultPatientSource()
    private val office = defaultOffice()
    private val treatment = defaultTreatment()

    private lateinit var treatmentService: TreatmentService
    private lateinit var treatments: Treatments

    @Before
    fun setUp() {
        treatments = mockTreatments()
        treatmentService = TreatmentService(treatments) { treatmentId }
    }

    @Test
    fun `init a treatment successful`() {
        val newTreatment = treatmentService.initTreatment(professional, patientName, patientSource, office)
        assertTreatment(newTreatment)
    }

    @Test(expected = ObjectAlreadyExistsException::class)
    fun `init a treatment with a patient that already is on treatment`() {
        givenTreatmentForPatient()
        treatmentService.initTreatment(professional, patientName, patientSource, office)
    }

    @Test
    fun `add a new session`() {
        givenTreatmentForPatient()
        val session = treatmentService.newSession(treatment, date, office, patientSourceFee, notPaid)
        assertNewSession(treatment, session)
    }

    private fun assertNewSession(treatment: Treatment, session: Session) {
        assertThat(session).isNotNull
        assertThat(treatment.sessions()).containsExactly(mutableListOf(session))
        assertThat(session.date).isEqualTo(date)
        assertThat(session.fee).isEqualTo(patientSourceFee)
        assertThat(session.officeId).isEqualTo(officeId)
        assertThat(session.paid).isEqualTo(notPaid)
        assertThat(session.number).isEqualTo(firstSession)
    }

    private fun givenTreatmentForPatient() {
        Mockito.`when`(treatments.findByPatientName(patientName)).thenReturn(treatment)
    }

    private fun mockTreatments(): Treatments = Mockito.mock(Treatments::class.java)

    private fun assertTreatment(treatment: Treatment) {
        with(treatment) {
            assertThat(id).isEqualTo(treatmentId)
            assertThat(patient).isEqualTo(patient)
            assertThat(office).isEqualTo(office)
            assertThat(derivation.patientSourceId).isEqualTo(patientSource.id)
            assertThat(derivation.currentFee).isEqualTo(patientSource.fee)
        }
    }

}


