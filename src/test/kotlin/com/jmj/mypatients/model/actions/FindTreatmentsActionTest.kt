package com.jmj.mypatients.model.actions

import com.jmj.mypatients.*
import com.jmj.mypatients.infrastructure.treatment.InMemoryTreatments
import com.jmj.mypatients.model.professional.ProfessionalFinder
import com.jmj.mypatients.model.treatment.Treatments
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class FindTreatmentsActionTest {

    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var findTreatmentsAction: FindTreatmentsAction
    private lateinit var treatments: Treatments

    @Before
    fun setUp() {
        treatments = InMemoryTreatments()
        professionalFinder = ProfessionalFinder(createProfessionals(), createOffices(), createPatientSources(), treatments)
        findTreatmentsAction = FindTreatmentsAction(professionalFinder)
    }

    @Test
    fun `when there are no treatments returns empty`() {
        val treatments = findTreatmentsAction.execute(FindTreatmentsRequest(professionalId))
        Assertions.assertThat(treatments).isEmpty()
    }

    @Test
    fun `when there a treatment returns it`() {
        treatments.save(defaultTreatment())
        val treatments = findTreatmentsAction.execute(FindTreatmentsRequest(professionalId))
        Assertions.assertThat(treatments).isNotEmpty
        Assertions.assertThat(treatments.size).isEqualTo(1)
        treatments.firstOrNull()?.let {
            Assertions.assertThat(it.id).isEqualTo(treatmentId)
            Assertions.assertThat(it.officeId).isEqualTo(officeId)
            Assertions.assertThat(it.patient).isEqualTo(patientName)
            Assertions.assertThat(it.patientSourceId).isEqualTo(patientSourceId)
        }
    }

}


