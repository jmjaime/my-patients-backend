package com.jmj.mypatients.infrastructure.persistence.treatment.office

import com.jmj.mypatients.domain.professional.office.Office
import com.jmj.mypatients.domain.professional.office.Offices

class InMemoryOffices(private val offices: MutableMap<String, Office> = mutableMapOf()) : Offices {

    override fun findByProfessionalId(professionalId: String) = offices.values.filter { it.professionalId == professionalId }

    override fun findByIdAndProfessionalId(officeId: String, professionalId: String): Office? = offices.values.firstOrNull { it.id == officeId && it.professionalId == professionalId }

    override fun find(officeId: String): Office? = offices[officeId]

    override fun save(office: Office) {
        offices[office.id] = office
    }
}