package com.jmj.mypatients.infrastructure.treatment.office

import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices

class InMemoryOffices(private val offices: MutableMap<Long, Office> = mutableMapOf()) : Offices {

    override fun findByIdAndProfessionalId(officeId: Long, professionalId: Long): Office? = offices.values.firstOrNull { it.id == officeId && it.professionalId == professionalId }

    override fun find(officeId: Long): Office? = offices[officeId]
}