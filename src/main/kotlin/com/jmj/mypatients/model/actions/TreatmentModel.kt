package com.jmj.mypatients.model.actions

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.model.treatment.Treatment

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TreatmentModel(val id: String, val officeId: String, val patientSourceId: String, val patient: String)

fun Treatment.toModel() = TreatmentModel(this.id, this.defaultOfficeId, this.derivation.patientSourceId, this.patient.name)
