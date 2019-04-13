package com.jmj.mypatients.model.actions

import com.jmj.mypatients.*
import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService
import com.jmj.mypatients.model.treatment.Treatments
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class NewSessionActionTest {

    private val notPaid = false
    private val date = LocalDate.now()
    private lateinit var newSessionAction: NewSessionAction
    private lateinit var treatmentService: TreatmentService
    private lateinit var professionalFinder: ProfessionalFinder

    @Before
    fun setUp() {
        val treatments = createTreatments()
        professionalFinder = createProfessionalFinder(treatments)
        treatmentService = TreatmentService(treatments) { sessionId }
        newSessionAction = NewSessionAction(treatmentService, professionalFinder)
    }

    private fun createProfessionalFinder(treatments: Treatments) = ProfessionalFinder(createProfessionals(), createOffices(), createPatientSources(), treatments)

    @Test
    fun `new session`() {
        val request = givenANewSessionRequest()
        val sessionModel = newSessionAction.execute(request)
        assertSession(sessionModel, request)
    }

    private fun assertSession(sessionModel: SessionModel, request: NewSessionRequest) {
        Assertions.assertThat(sessionModel.date).isEqualTo(request.date)
        Assertions.assertThat(sessionModel.fee).isEqualTo(request.fee)
        Assertions.assertThat(sessionModel.number).isEqualTo(firstSession)
        Assertions.assertThat(sessionModel.officeId).isEqualTo(request.officeId)
        Assertions.assertThat(sessionModel.paid).isEqualTo(request.paid)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `new session with invalid office`() {
        val request = givenANewSessionRequest(office = notExists)
        newSessionAction.execute(request)
    }


    @Test(expected = ObjectNotFoundException::class)
    fun `new session with invalid professional`() {
        val request = givenANewSessionRequest( professional = notExists)
        newSessionAction.execute(request)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `new session with invalid treatment`() {
        val request = givenANewSessionRequest(treatment = notExists)
        newSessionAction.execute(request)
    }

    private fun givenANewSessionRequest(professional: String = professionalId, treatment: String = treatmentId, office: String = officeId, patient: String = patientId) =
            NewSessionRequest(professional, treatment, office, patient, date, patientSourceFee.value, notPaid)

}