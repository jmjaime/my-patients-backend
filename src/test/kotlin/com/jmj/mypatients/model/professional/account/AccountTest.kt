package com.jmj.mypatients.model.professional.account

import com.jmj.mypatients.model.money.Money
import org.assertj.core.api.Assertions
import org.junit.Test
import java.time.Instant

class AccountTest {

    private val moneyOperation = MoneyOperation(Money(10), Instant.now())

    @Test
    fun `add credit should increment credit`() {
        val account = givenAnAccount()
        account.addCredit(moneyOperation)
        thenAccountHasCredit(account, moneyOperation.amount)
    }

    @Test
    fun `add debit should increment debit`() {
        val account = givenAnAccount()
        account.addDebit(moneyOperation)
        thenAccountHasDebit(account, moneyOperation.amount)
    }

    private fun thenAccountHasCredit(account: Account, expectedCredit: Money) {
        Assertions.assertThat(account.credit()).isEqualTo(expectedCredit)
        Assertions.assertThat(account.debit()).isEqualTo(Money.ZERO)
        Assertions.assertThat(account.movements().size).isEqualTo(1)
        Assertions.assertThat(account.balance()).isEqualTo(expectedCredit)
        val movement = account.movements().first()
        Assertions.assertThat(movement.date).isEqualTo(moneyOperation.date)
        Assertions.assertThat(movement.movementType).isEqualTo(MovementType.CREDIT)
        Assertions.assertThat(movement.number).isEqualTo(1)
        Assertions.assertThat(movement.amount).isEqualTo(expectedCredit)
    }

    private fun thenAccountHasDebit(account: Account, expectedDebit: Money) {
        Assertions.assertThat(account.credit()).isEqualTo(Money.ZERO)
        Assertions.assertThat(account.debit()).isEqualTo(expectedDebit)
        Assertions.assertThat(account.movements().size).isEqualTo(1)
        Assertions.assertThat(account.balance()).isEqualTo(expectedDebit.negate())
        val movement = account.movements().first()
        Assertions.assertThat(movement.date).isEqualTo(moneyOperation.date)
        Assertions.assertThat(movement.movementType).isEqualTo(MovementType.DEBIT)
        Assertions.assertThat(movement.number).isEqualTo(1)
        Assertions.assertThat(movement.amount).isEqualTo(expectedDebit)
    }


    private fun givenAnAccount() = Account()
}