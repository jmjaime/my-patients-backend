package com.jmj.mypatients.domain.treatment

import com.jmj.mypatients.*
import com.jmj.mypatients.domain.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.domain.events.EventPublisher
import com.jmj.mypatients.domain.events.SessionCreated
import com.jmj.mypatients.domain.treatment.session.Session
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.time.Clock

class TreatmentServiceTest {

    private val date = Clock.systemDefaultZone().instant()
    private val notPaid = false
    private val professional = defaultProfessional()
    private val patientSource = defaultPatientSource()
    private val office = defaultOffice()
    private val treatment = defaultTreatment()

    private lateinit var treatmentService: TreatmentService
    private lateinit var treatments: Treatments
    private lateinit var eventPublisher: EventPublisher


    @Before
    fun setUp() {
        treatments = mockTreatments()
        eventPublisher = Mockito.mock(EventPublisher::class.java)
        treatmentService = TreatmentService(treatments, eventPublisher) { treatmentId }
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
        assertThat(treatment.sessions()).containsExactly(session)
        assertThat(session.date).isEqualTo(date)
        assertThat(session.fee).isEqualTo(patientSourceFee)
        assertThat(session.officeId).isEqualTo(officeId)
        assertThat(session.paid).isEqualTo(notPaid)
        assertThat(session.number).isEqualTo(firstSession)
        val expectedSessionCreatedEvent = SessionCreated(professionalId, treatmentId, 1)
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(expectedSessionCreatedEvent)
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


