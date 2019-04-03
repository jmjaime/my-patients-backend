package com.jmj.mypatients.infrastructure.professional

import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.Professionals

class InMemoryProfessionals(private val professionals: MutableMap<Long, Professional> = mutableMapOf()) : Professionals{
    private var lastId = 0L

    override fun find(treatmentId: Long): Professional? = professionals[treatmentId]

    override fun save(professional: Professional) {
        professionals[professional.id] = professional
    }

    override fun nextId(): Long {
        lastId = lastId.inc()
        return lastId
    }


}