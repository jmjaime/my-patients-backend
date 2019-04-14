package com.jmj.mypatients.model.actions

import com.jmj.mypatients.*
import com.jmj.mypatients.infrastructure.treatment.InMemoryTreatments
import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.TreatmentService
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class InitTreatmentActionTest {

    private lateinit var treatmentService: TreatmentService
    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var initTreatmentAction: InitTreatmentAction

    @Before
    fun setUp() {
        val treatments = InMemoryTreatments()
        treatmentService = TreatmentService(treatments) { treatmentId }
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
        Assertions.assertThat(treatment.office.description).isEqualTo(officeName)
        Assertions.assertThat(treatment.patient).isEqualTo(patientName)
        Assertions.assertThat(treatment.derivation.patientSourceId).isEqualTo(patientSourceId)
        Assertions.assertThat(treatment.derivation.currentFee).isEqualTo(patientSourceFee.value)

    }

    private fun givenARequest(office: String = officeId, patientSource: String = patientSourceId, patient: String = patientName) = InitTreatmentRequest(professionalId, office, patientSource, patient)
}


