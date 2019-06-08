package com.jmj.mypatients.domain.professional

import com.jmj.mypatients.domain.errors.ObjectNotFoundException
import com.jmj.mypatients.domain.professional.derivation.PatientSource
import com.jmj.mypatients.domain.professional.derivation.PatientSources
import com.jmj.mypatients.domain.professional.office.Office
import com.jmj.mypatients.domain.professional.office.Offices
import com.jmj.mypatients.domain.treatment.Treatment
import com.jmj.mypatients.domain.treatment.Treatments

class ProfessionalFinder(private val professionals: Professionals, private val offices: Offices, private val patientSources: PatientSources, private val treatments: Treatments) {

    fun findProfessionalById(professionalId: String): Professional = professionals.find(professionalId)
            ?: throw ObjectNotFoundException("Professional $professionalId not found")

    fun findOfficeByProfessionalAndId(professional: Professional, officeId: String): Office =
            offices.findByIdAndProfessionalId(officeId, professional.id)
                    ?: throw ObjectNotFoundException("Office $officeId not found")

    fun findPatientSourceByProfessionalAndId(professional: Professional, patientSourceId: String): PatientSource =
            patientSources.findByIdAndProfessionalId(patientSourceId, professional.id)
                    ?: throw ObjectNotFoundException("Patient Source $patientSourceId not found")

    fun findTreatmentByProfessionalAndId(professional: Professional, treatmentId: String): Treatment = treatments.findByIdAndProfessionalId(treatmentId, professional.id)
            ?: throw ObjectNotFoundException("Treatment $treatmentId not found")

    fun findTreatmentsByProfessional(professional: Professional) = treatments.findByProfessional(professional.id)

    fun findOfficesByProfessional(professional: Professional) = offices.findByProfessionalId(professional.id)

    fun findPatientSourcesByProfessional(professional: Professional) = patientSources.findByProfessionalId(professional.id)
}