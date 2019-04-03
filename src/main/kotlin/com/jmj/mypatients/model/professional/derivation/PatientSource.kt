package com.jmj.mypatients.model.professional.derivation

import com.jmj.mypatients.model.money.Money

data class PatientSource(val id: Long, val name: String, val fee: Money, val tax: Double, val professionalId: Long)
