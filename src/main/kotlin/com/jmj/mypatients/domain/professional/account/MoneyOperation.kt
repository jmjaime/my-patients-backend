package com.jmj.mypatients.domain.professional.account

import com.jmj.mypatients.domain.money.Money
import java.time.Instant

data class MoneyOperation(val amount: Money, val date: Instant)