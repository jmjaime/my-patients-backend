package com.jmj.mypatients.actions.models

import com.jmj.mypatients.domain.professional.derivation.PatientSource
import java.math.BigDecimal

data class PatientSourceModel(val id: String,
                              val source: String,
                              val fee: BigDecimal,
                              val tax: BigDecimal)

fun PatientSource.toModel(): PatientSourceModel = PatientSourceModel(id, source, fee.value, tax)
