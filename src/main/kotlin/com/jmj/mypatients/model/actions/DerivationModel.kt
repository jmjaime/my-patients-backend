package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.professional.derivation.Derivation
import java.math.BigDecimal

data class DerivationModel(val patientSourceId: String, val currentFee: BigDecimal)

fun Derivation.toModel() = DerivationModel(this.patientSourceId, this.currentFee.value)