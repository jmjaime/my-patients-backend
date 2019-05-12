package com.jmj.mypatients.model.professional

import com.jmj.mypatients.model.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.professional.account.Account
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccount
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.model.professional.account.office.OfficeAccount
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccount
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccounts
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices
import java.math.BigDecimal

class ProfessionalInitializer(private val professionals: Professionals,
                              private val offices: Offices,
                              private val patientSources: PatientSources,
                              private val professionalAccounts: ProfessionalAccounts,
                              private val officeAccounts: OfficeAccounts,
                              private val patientSourceAccounts: PatientSourceAccounts,
                              private val idGenerator: () -> String) {


    fun newProfessional(professionalName: String) =
            createProfessional(professionalName).also {
                createVirtualOffice(it)
                createParticularPatientSource(it)
            }


    fun newOffice(professional: Professional, officeName: String, cost: Money) {

    }

    fun newPatientSource(professional: Professional, sourceName: String, fee: Money, tax: BigDecimal) {

    }

    private fun createProfessional(professionalName: String) =
            Professional(idGenerator(), professionalName).also {
                professionals.save(it)
                accountFor(it)
            }

    private fun createVirtualOffice(professional: Professional) {
        val virtualOffice = Office.virtual(idGenerator(), professional.id)
        offices.save(virtualOffice)
        accountFor(professional, virtualOffice)
    }

    private fun createParticularPatientSource(professional: Professional) {
        val particularPatientSource = PatientSource.particular(idGenerator(), professional.id)
        patientSources.save(particularPatientSource)
        accountFor(professional, particularPatientSource)
    }

    private fun accountFor(professional: Professional) {
        professionalAccounts.findByProfessional(professional.id)?.run { throw ObjectAlreadyExistsException("Already exists an account for professional ${professional.id}") }
        professionalAccounts.save(ProfessionalAccount(professional.id, Account()))
    }

    private fun accountFor(professional: Professional, office: Office) {
        officeAccounts.findByProfessionalAndId(professional.id, office.id)?.run { throw ObjectAlreadyExistsException("Already exists an account for professional ${professional.id} and office ${office.id}") }
        val officeAccount = OfficeAccount(idGenerator(), professional.id, office.id, Account())
        officeAccounts.save(officeAccount)
    }

    private fun accountFor(professional: Professional, patientSource: PatientSource) {
        patientSourceAccounts.findByProfessionalAndId(professional.id, patientSource.id)?.run { throw ObjectAlreadyExistsException("Already exists an account for professional ${professional.id} and patient source ${patientSource.id}") }
        val patientSourceAccount = PatientSourceAccount(idGenerator(), professional.id, patientSource.id, Account())
        patientSourceAccounts.save(patientSourceAccount)
    }

}