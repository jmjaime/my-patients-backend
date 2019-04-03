package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.model.professional.Professional

class TreatmentFinder(private val treatments: Treatments) {

    fun findTreatmentByProfessional(professional: Professional) = treatments.findByProfessional(professional.id)
}