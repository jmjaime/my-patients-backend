package com.jmj.mypatients.model.professional.account

import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.professionalId
import org.assertj.core.api.Assertions
import org.junit.Test
import java.time.Clock

class AccountTest {

    private val now = Clock.systemDefaultZone().instant()
    private val sessionValue = Money(10)

    @Test
    fun `add credit should increment credit`() {
        val account = givenAnAccount()
        account.addCredit(now, sessionValue)
        thenAccountHasCredit(account, sessionValue)
    }

    private fun thenAccountHasCredit(account: Account, expectedCredit: Money) {
        Assertions.assertThat(account.credit()).isEqualTo(expectedCredit)
        Assertions.assertThat(account.debit()).isEqualTo(Money.ZERO)
        Assertions.assertThat(account.movements().size).isEqualTo(1)
        val movement = account.movements().first()
        Assertions.assertThat(movement.date).isEqualTo(now)
        Assertions.assertThat(movement.movementType).isEqualTo(MovementType.CREDIT)
        Assertions.assertThat(movement.number).isEqualTo(1)
        Assertions.assertThat(movement.value).isEqualTo(expectedCredit)
    }

    private fun givenAnAccount() = Account(professionalId)
}