package com.jmj.mypatients.model.professional.derivation

import com.jmj.mypatients.model.money.Money
import java.math.BigDecimal

data class PatientSource(val id: String, val name: String, val fee: Money, val tax: BigDecimal, val professionalId: String)
