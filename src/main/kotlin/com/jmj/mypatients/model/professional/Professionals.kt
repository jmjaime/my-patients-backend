package com.jmj.mypatients.model.professional

interface Professionals {

    fun find(professionalId: String): Professional?

    fun save(professional: Professional)
}