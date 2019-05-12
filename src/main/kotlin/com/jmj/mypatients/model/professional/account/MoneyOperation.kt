package com.jmj.mypatients.model.professional.account

import com.jmj.mypatients.model.money.Money
import java.time.Instant

data class MoneyOperation(val value: Money, val date: Instant)