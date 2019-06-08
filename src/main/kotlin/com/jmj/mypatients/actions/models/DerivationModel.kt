package com.jmj.mypatients.actions.models

import com.jmj.mypatients.domain.professional.derivation.Derivation
import java.math.BigDecimal

data class DerivationModel(val patientSourceId: String, val currentFee: BigDecimal)

fun Derivation.toModel() = DerivationModel(this.patientSourceId, this.currentFee.value)