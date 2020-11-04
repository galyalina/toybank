package com.iotta.bank.persistence

import com.iotta.bank.model.Account
import com.iotta.bank.model.AccountType
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap


interface AccountRepository {

    fun getAccount(iban: String): Account?
    fun getBalance(iban: String): Double?
    fun updateBalance(iban: String, amount: Double): Boolean

    @Repository
    class InMemoryImpl : AccountRepository {

        private final val accounts: ConcurrentHashMap<String, Account> = ConcurrentHashMap()

        init {
            accounts["1"] = Account("1", AccountType.CHECKING, emptyList(), 1000.0)
            accounts["2"] = Account("2", AccountType.CHECKING, emptyList(), 0.0)
            accounts["3"] = Account("3", AccountType.LOAN, emptyList(), 2000.0)
            accounts["4"] = Account("4", AccountType.SAVING, listOf("1"), 5000.0)
        }

        override fun getAccount(iban: String): Account? {
            return accounts[iban]
        }

        override fun getBalance(iban: String): Double? {
            return accounts[iban]?.balance
        }

        override fun updateBalance(iban: String, amount: Double): Boolean {
            accounts[iban]?.let {
                accounts[iban] = it.copy(balance = it.balance + amount)
                return true
            } ?: return false
        }

    }
}
