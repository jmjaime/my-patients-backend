package com.jmj.mypatients.actions

import com.jmj.mypatients.*
import com.jmj.mypatients.domain.errors.ObjectNotFoundException
import com.jmj.mypatients.domain.professional.ProfessionalFinder
import com.jmj.mypatients.domain.professional.Professionals
import com.jmj.mypatients.infrastructure.persistence.professional.InMemoryProfessionals
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class FindProfessionalActionTest {

    private lateinit var professionals: Professionals
    private lateinit var professionalFinder: ProfessionalFinder
    private lateinit var findProfessionalAction: FindProfessionalAction

    @Before
    fun setUp() {
        professionals = InMemoryProfessionals()
        professionalFinder = ProfessionalFinder(professionals, createOffices(), createPatientSources(), createTreatments())
        findProfessionalAction = FindProfessionalAction(professionalFinder)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun `when there are no professional throw notFound `() {
        findProfessionalAction(FindProfessionalRequest(professionalId))
    }

    @Test
    fun `when founds the professional returns it`() {
        professionals.save(defaultProfessional())
        val professional = findProfessionalAction(FindProfessionalRequest(professionalId))
        Assertions.assertThat(professional.id).isEqualTo(professionalId)
        Assertions.assertThat(professional.name).isEqualTo(professionalName)
    }

}


