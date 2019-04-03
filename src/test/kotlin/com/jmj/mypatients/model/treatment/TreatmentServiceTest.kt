package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.model.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.treatment.account.Account
import com.jmj.mypatients.model.professional.derivation.Derivation
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.professional.Professional
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class TreatmentServiceTest {

    private val patient = Patient(patientId, patientName, professionalId)
    private val professional = Professional(professionalId, professionalName)
    private val patientSource = PatientSource(1L, "Particular", Money(100), "0".toDouble(), patientId)
    private val defaultOffice = Office(officeId, "office", patientId)
    private val treatment = Treatment(treatmentId, professionalId ,patientId, Account(), defaultOffice, Derivation(patientSource, patientSource.fee))

    private lateinit var treatmentService: TreatmentService
    private lateinit var treatments: Treatments


    @Before
    fun setUp() {
        treatments = mockTreatments()
        treatmentService = TreatmentService(treatments)
    }

    @Test
    fun `init a treatment successful`() {
        val newTreatment = treatmentService.initTreatment(professional, patient, patientSource, defaultOffice)
        assertTreatment(newTreatment)
    }

    @Test(expected = ObjectAlreadyExistsException::class)
    fun `init a treatment with a patient that already is on treatment`() {
        givenTreatmentForPatient(patient)
        treatmentService.initTreatment(professional, patient, patientSource, defaultOffice)
    }

    private fun givenTreatmentForPatient(patient: Patient) {
        Mockito.`when`(treatments.findByPatient(patient)).thenReturn(treatment)
    }

    private fun mockTreatments(): Treatments {
        val treatments = Mockito.mock(Treatments::class.java)
        Mockito.`when`(treatments.nextId()).thenReturn(treatmentId)
        return treatments
    }

    private fun assertTreatment(treatment: Treatment) {
        with(treatment) {
            Assertions.assertThat(id).isEqualTo(treatmentId)
            Assertions.assertThat(patient).isEqualTo(patient)
            Assertions.assertThat(defaultOffice).isEqualTo(defaultOffice)
            Assertions.assertThat(derivation.patientSource).isEqualTo(patientSource)
            Assertions.assertThat(derivation.currentFee).isEqualTo(patientSource.fee)
        }
    }

}

private const val professionalId = 8L
private const val professionalName = "professional"
private const val patientId = 56L
private const val patientName = "patient"
private const val officeId = 10L
private const val treatmentId = 1L
