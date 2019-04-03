package com.jmj.mypatients.model.actions

import com.jmj.mypatients.infrastructure.patient.InMemoryPatients
import com.jmj.mypatients.infrastructure.professional.InMemoryProfessionals
import com.jmj.mypatients.infrastructure.treatment.InMemoryTreatments
import com.jmj.mypatients.infrastructure.treatment.derivation.InMemoryPatientSources
import com.jmj.mypatients.infrastructure.treatment.office.InMemoryOffices
import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.patient.PatientFinder
import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.professional.Professionals
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices
import com.jmj.mypatients.model.treatment.TreatmentService
import com.jmj.mypatients.model.treatment.Treatments
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class InitTreatmentActionTest {

    private val office = Office(officeId, "office", professionalId)
    private val professional = Professional(professionalId, "professional")
    private val patient = Patient(patientId, "patient", professionalId)

    private val patientSource = PatientSource(patientSourceId, "Particular", Money(100), 0.toDouble(), professionalId)

    private lateinit var treatmentService: TreatmentService
    private lateinit var patientFinder: PatientFinder
    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var initTreatmentAction: InitTreatmentAction

    @Before
    fun setUp() {
        val treatments = InMemoryTreatments()
        treatmentService = TreatmentService(treatments)
        patientFinder = createPatientFinder()
        professionalFinder = createProfessionalFinder(treatments)
        initTreatmentAction = InitTreatmentAction(treatmentService, professionalFinder, patientFinder)
    }

    @Test
    fun `init a treatment`() {
        val initTreatmentRequest = givenARequest()
        val treatment = initTreatmentAction.execute(initTreatmentRequest)
        assertTreatment(treatment)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `init a treatment with invalid office`() {
        val initTreatmentRequest = givenARequest(office = 999L)
        initTreatmentAction.execute(initTreatmentRequest)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `init a treatment with invalid patient source`() {
        val initTreatmentRequest = givenARequest(patientSource = 999L)
        initTreatmentAction.execute(initTreatmentRequest)
    }

    private fun assertTreatment(treatment: TreatmentModel) {
        Assertions.assertThat(treatment.id).isEqualTo(treatmentId)
        Assertions.assertThat(treatment.officeId).isEqualTo(officeId)
        Assertions.assertThat(treatment.patientId).isEqualTo(patientId)
        Assertions.assertThat(treatment.patientSourceId).isEqualTo(patientSourceId)

    }

    private fun createProfessionalFinder(treatments: Treatments) = ProfessionalFinder(createProfessionals(), createOffices(), createPatientSources(), treatments)

    private fun createProfessionals() = InMemoryProfessionals(mutableMapOf(Pair(professional.id, professional)))

    private fun createPatientFinder() = PatientFinder(InMemoryPatients(mutableMapOf(Pair(patient.id, patient))))

    private fun givenARequest(office: Long = officeId, patientSource: Long = patientSourceId, patient: Long = patientId) = InitTreatmentRequest(professionalId, office, patientSource, patient)

    private fun createPatientSources() = InMemoryPatientSources(mutableMapOf(Pair(patientSourceId, patientSource)))

    private fun createOffices(): Offices = InMemoryOffices(mutableMapOf(Pair(officeId, office)))

}

private const val treatmentId = 1L
private const val professionalId = 87L
private const val officeId = 66L
private const val patientSourceId = 24L
private const val patientId = 1L
