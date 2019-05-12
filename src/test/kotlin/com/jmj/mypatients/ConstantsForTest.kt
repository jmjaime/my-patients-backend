package com.jmj.mypatients

import com.jmj.mypatients.infrastructure.professional.InMemoryProfessionals
import com.jmj.mypatients.infrastructure.professional.account.InMemoryProfessionalAccounts
import com.jmj.mypatients.infrastructure.professional.account.derivation.InMemoryPatientSourceAccounts
import com.jmj.mypatients.infrastructure.professional.account.office.InMemoryOfficeAccounts
import com.jmj.mypatients.infrastructure.treatment.InMemoryTreatments
import com.jmj.mypatients.infrastructure.treatment.derivation.InMemoryPatientSources
import com.jmj.mypatients.infrastructure.treatment.office.InMemoryOffices
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.professional.Professional
import com.jmj.mypatients.model.professional.Professionals
import com.jmj.mypatients.model.professional.account.Account
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccount
import com.jmj.mypatients.model.professional.account.derivation.PatientSourceAccounts
import com.jmj.mypatients.model.professional.account.office.OfficeAccount
import com.jmj.mypatients.model.professional.account.office.OfficeAccounts
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccount
import com.jmj.mypatients.model.professional.account.professional.ProfessionalAccounts
import com.jmj.mypatients.model.professional.derivation.Derivation
import com.jmj.mypatients.model.professional.derivation.PatientSource
import com.jmj.mypatients.model.professional.derivation.PatientSources
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.professional.office.Offices
import com.jmj.mypatients.model.treatment.Treatment
import com.jmj.mypatients.model.treatment.Treatments
import java.math.BigDecimal.TEN

const val notExists = "XXX"

const val treatmentId = "1"
const val firstSession = 1

const val professionalId = "87"
const val professionalName = "professional"

const val officeId = "66"
const val officeName = "Office1"

const val patientSourceId = "24"
const val patientSourceName = "Particular"

const val officeAccountId = "34"
const val patientSourceAccountId = "70"

val patientSourceTax = TEN
val patientSourceFee = Money(100)

const val patientId = "1"
const val patientName = "John"

const val sessionId = "77"


fun createProfessionals(professionals: List<Professional> = listOf(defaultProfessional())): Professionals =
        InMemoryProfessionals().apply { professionals.forEach { save(it) } }

fun createOffices(offices: List<Office> = listOf(defaultOffice())): Offices =
        InMemoryOffices().apply { offices.forEach { save(it) } }

fun createPatientSources(patientSources: List<PatientSource> = listOf(defaultPatientSource())): PatientSources =
        InMemoryPatientSources().apply { patientSources.forEach { save(it) } }

fun createTreatments(treatments: List<Treatment> = listOf(defaultTreatment())): Treatments =
        InMemoryTreatments().apply { treatments.forEach { save(it) } }

fun createProfessionalAccounts(professionalAccounts: List<ProfessionalAccount> = listOf(defaultProfessionalAccount())): ProfessionalAccounts =
        InMemoryProfessionalAccounts().apply { professionalAccounts.forEach { save(it) } }

fun createOfficeAccounts(officeAccounts: List<OfficeAccount> = listOf(defaultOfficeAccount())): OfficeAccounts =
        InMemoryOfficeAccounts().apply { officeAccounts.forEach { save(it) } }

fun createPatientSourceAccounts(patientSourceAccounts: List<PatientSourceAccount> = listOf(defaultPatientSourceAccount())): PatientSourceAccounts =
        InMemoryPatientSourceAccounts().apply { patientSourceAccounts.forEach { save(it) } }

fun defaultProfessional() = Professional(professionalId, professionalName)
fun defaultOffice() = Office(officeId, officeName, professionalId, Money(100))
fun defaultPatientSource() = PatientSource(patientSourceId, patientSourceName, patientSourceFee, patientSourceTax, professionalId)
fun defaultDerivation() = Derivation(patientSourceId, patientSourceFee)
fun defaultTreatment() = Treatment(treatmentId, professionalId, defaultPatient(), officeId, defaultDerivation(), mutableListOf())
fun defaultPatient() = Patient(patientId, patientName)
fun defaultOfficeAccount() = OfficeAccount(officeAccountId, professionalId, officeId, Account())
fun defaultPatientSourceAccount() = PatientSourceAccount(patientSourceAccountId, professionalId, patientSourceId, Account())
fun defaultProfessionalAccount() = ProfessionalAccount(professionalId, Account())

