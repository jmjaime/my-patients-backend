package com.jmj.mypatients.model.actions

import com.jmj.mypatients.*
import com.jmj.mypatients.model.actions.models.SessionModel
import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.events.EventPublisher
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService
import com.jmj.mypatients.model.treatment.Treatments
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.time.Clock

class AddSessionActionTest {

    private val notPaid = false
    private val date = Clock.systemDefaultZone().instant()
    private lateinit var addSessionAction: AddSessionAction
    private lateinit var treatmentService: TreatmentService
    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var eventPublisher: EventPublisher

    @Before
    fun setUp() {
        val treatments = createTreatments()
        professionalFinder = createProfessionalFinder(treatments)
        eventPublisher = Mockito.mock(EventPublisher::class.java)
        treatmentService = TreatmentService(treatments, eventPublisher) { sessionId }
        addSessionAction = AddSessionAction(treatmentService, professionalFinder)
    }

    private fun createProfessionalFinder(treatments: Treatments) = ProfessionalFinder(createProfessionals(), createOffices(), createPatientSources(), treatments)

    @Test
    fun `new session`() {
        val request = givenANewSessionRequest()
        val sessionModel = addSessionAction(request)
        assertSession(sessionModel, request)
    }

    private fun assertSession(sessionModel: SessionModel, request: AddSessionRequest) {
        assertThat(sessionModel.date).isEqualTo(request.date)
        assertThat(sessionModel.fee).isEqualTo(request.fee)
        assertThat(sessionModel.number).isEqualTo(firstSession)
        assertThat(sessionModel.officeId).isEqualTo(request.officeId)
        assertThat(sessionModel.paid).isEqualTo(request.paid)
        assertThat(sessionModel.treatmentId).isEqualTo(request.treatmentId)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `new session with invalid office`() {
        val request = givenANewSessionRequest(office = notExists)
        addSessionAction(request)
    }


    @Test(expected = ObjectNotFoundException::class)
    fun `new session with invalid professional`() {
        val request = givenANewSessionRequest(professional = notExists)
        addSessionAction(request)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `new session with invalid treatment`() {
        val request = givenANewSessionRequest(treatment = notExists)
        addSessionAction(request)
    }

    private fun givenANewSessionRequest(professional: String = professionalId, treatment: String = treatmentId, office: String = officeId) =
            AddSessionRequest(professional, treatment, office, date, patientSourceFee.value, notPaid)

}