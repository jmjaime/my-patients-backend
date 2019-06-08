package com.jmj.mypatients.domain.professional.office

interface Offices {
    fun find(officeId: String): Office?
    fun findByIdAndProfessionalId(officeId: String, professionalId: String) : Office?
    fun findByProfessionalId(professionalId: String): List<Office>
    fun save(office: Office)
}