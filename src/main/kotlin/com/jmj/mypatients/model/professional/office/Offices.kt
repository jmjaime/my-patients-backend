package com.jmj.mypatients.model.professional.office

interface Offices {
    fun find(officeId: String): Office?
    fun findByIdAndProfessionalId(officeId: String, professionalId: String) : Office?
    fun save(office: Office)
}