package com.jmj.mypatients.model.professional.office

interface Offices {
    fun find(officeId: Long): Office?

    fun findByIdAndProfessionalId(officeId: Long, professionalId: Long) : Office?
}