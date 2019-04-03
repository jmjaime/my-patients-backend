package com.jmj.mypatients.model.actions

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.jmj.mypatients.model.treatment.Treatment

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TreatmentModel(val id: Long, val officeId: Long, val patientSourceId: Long, val patientId: Long)

fun Treatment.toModel() = TreatmentModel(this.id, this.defaultOffice.id, this.derivation.patientSource.id, this.patientId)
