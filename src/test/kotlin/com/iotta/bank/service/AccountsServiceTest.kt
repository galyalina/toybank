package com.iotta.bank.service

import com.iotta.bank.model.Account
import com.iotta.bank.model.AccountType
import com.iotta.bank.persistence.AccountRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

internal class AccountsServiceTest {

    private val account = Account("1", AccountType.CHECKING, emptyList(), 1000.0)
    private val transactionId = "dummy-transaction-id"

    private val repository: AccountRepository = mockk()

    private val transferValidator: TransferValidator = mockk()

    private val transactionsService: TransactionsService = mockk()

    private val service: AccountsService = AccountsService.Impl(repository, transferValidator, transactionsService)


    @BeforeEach
    fun setUp() {
        every {
            repository.getAccount(any())
        } returns account

        // Each update balance will fail on account with IBAN = 1
        every {
            repository.updateBalance("1", any())
        } returns false

        // Each update balance will success on account with IBAN = 2
        every {
            repository.updateBalance("2", any())
        } returns true

        every {
            transactionsService.submitDepositTransaction(any(), any())
        } returns transactionId

        every {
            transactionsService.submitTransferTransaction(any(), any(), any())
        } returns transactionId

        every {
            transactionsService.finishTransaction(any(), any())
        } returns true
    }

    @Test
    fun `get account`() {
        val iban = "1"
        service.getAccount(iban)
        verify(exactly = 1) {
            repository.getAccount(iban)
        }
    }

    @Test
    fun `that false is returned when deposit fails`() {
        val result = service.deposit("1", 10.0)
        assertThat(result).isFalse()
    }

    @Test
    fun `that true is returned when deposit succeeds`() {
        val result = service.deposit("2", 10.0)
        assertThat(result).isTrue()
    }

    @Test
    fun `transfer`() {
    }

    @Test
    fun `balance`() {
    }
}
