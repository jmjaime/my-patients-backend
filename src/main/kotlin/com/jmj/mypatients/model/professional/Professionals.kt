package com.jmj.mypatients.model.professional

interface Professionals {

    fun nextId(): Long

    fun find(professionalId: Long): Professional?

    fun save(professional: Professional)
}