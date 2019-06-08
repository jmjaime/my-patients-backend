package com.jmj.mypatients.infrastructure.persistence.professional

import com.jmj.mypatients.domain.professional.Professional
import com.jmj.mypatients.domain.professional.Professionals

class InMemoryProfessionals(private val professionals: MutableMap<String, Professional> = mutableMapOf()) : Professionals{

    override fun find(professionalId: String): Professional? = professionals[professionalId]

    override fun save(professional: Professional) {
        professionals[professional.id] = professional
    }

}