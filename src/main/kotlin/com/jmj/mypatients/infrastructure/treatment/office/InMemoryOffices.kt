package com.jmj.mypatients.infrastructure.treatment.office

import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices

class InMemoryOffices(private val offices: MutableMap<String, Office> = mutableMapOf()) : Offices {

    override fun findByIdAndProfessionalId(officeId: String, professionalId: String): Office? = offices.values.firstOrNull { it.id == officeId && it.professionalId == professionalId }

    override fun find(officeId: String): Office? = offices[officeId]

    override fun save(office: Office) {
        offices[office.id] = office
    }
}