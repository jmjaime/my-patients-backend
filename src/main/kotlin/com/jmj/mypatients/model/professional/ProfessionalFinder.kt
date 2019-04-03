package com.jmj.mypatients.model.professional

import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices
import com.jmj.mypatients.model.treatment.Treatment
import com.jmj.mypatients.model.treatment.Treatments

class ProfessionalFinder(private val professionals: Professionals, private val offices: Offices, private val patientSources: PatientSources, private val treatments: Treatments) {

    fun findById(professionalId: Long): Professional = professionals.find(professionalId)
            ?: throw ObjectNotFoundException("Professional $professionalId not found")

    fun findProfessionalOfficeById(professional: Professional, officeId: Long) : Office =
            offices.findByIdAndProfessionalId(officeId, professional.id)
                    ?: throw ObjectNotFoundException("Office $officeId not found")

    fun findProfessionalPatientSourceById(professional: Professional, patientSourceId: Long): PatientSource =
            patientSources.findByIdAndProfessionalId(patientSourceId, professional.id)
                    ?: throw ObjectNotFoundException("Patient Source $patientSourceId not found")

    fun getProfessionalTreatmentById(professional: Professional, treatmentId: Long): Treatment = treatments.findByIdAndProfessionalId(treatmentId, professional.id)
            ?: throw ObjectNotFoundException("Treatment $treatmentId not found")

}