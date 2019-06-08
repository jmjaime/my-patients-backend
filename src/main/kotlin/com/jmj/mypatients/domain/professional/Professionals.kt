package com.jmj.mypatients.domain.professional

interface Professionals {

    fun find(professionalId: String): Professional?

    fun save(professional: Professional)
}