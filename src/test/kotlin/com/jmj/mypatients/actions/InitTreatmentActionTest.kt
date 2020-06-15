package com.jmj.mypatients.actions

import com.jmj.mypatients.*
import com.jmj.mypatients.actions.models.TreatmentModel
import com.jmj.mypatients.domain.errors.ObjectNotFoundException
import com.jmj.mypatients.domain.events.EventPublisher
import com.jmj.mypatients.domain.professional.ProfessionalFinder
import com.jmj.mypatients.domain.treatment.TreatmentService
import com.jmj.mypatients.infrastructure.persistence.treatment.InMemoryTreatments
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class InitTreatmentActionTest {

    private lateinit var treatmentService: TreatmentService
    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var initTreatmentAction: InitTreatmentAction
    private lateinit var eventPublisher: EventPublisher


    @Before
    fun setUp() {
        val treatments = InMemoryTreatments()
        eventPublisher = Mockito.mock(EventPublisher::class.java)
        treatmentService = TreatmentService(treatments, eventPublisher) { treatmentId }
        professionalFinder = ProfessionalFinder(createProfessionals(), createOffices(), createPatientSources(), treatments)
        initTreatmentAction = InitTreatmentAction(treatmentService, professionalFinder)
    }

    @Test
    fun `init a treatment`() {
        val initTreatmentRequest = givenARequest()
        val treatment = initTreatmentAction(initTreatmentRequest)
        assertTreatment(treatment)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `init a treatment with invalid office`() {
        val initTreatmentRequest = givenARequest(office = notExists)
        initTreatmentAction(initTreatmentRequest)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `init a treatment with invalid patient source`() {
        val initTreatmentRequest = givenARequest(patientSource = notExists)
        initTreatmentAction(initTreatmentRequest)
    }

    private fun assertTreatment(treatment: TreatmentModel) {
        Assertions.assertThat(treatment.id).isEqualTo(treatmentId)
        Assertions.assertThat(treatment.office.id).isEqualTo(officeId)
        Assertions.assertThat(treatment.office.name).isEqualTo(officeName)
        Assertions.assertThat(treatment.patient).isEqualTo(patientName)
        Assertions.assertThat(treatment.derivation.patientSourceId).isEqualTo(patientSourceId)
        Assertions.assertThat(treatment.derivation.currentFee).isEqualTo(patientSourceFee.value)

    }

    private fun givenARequest(office: String = officeId, patientSource: String = patientSourceId, patient: String = patientName) = InitTreatmentRequest(professionalId, office, patientSource, patient)
}


