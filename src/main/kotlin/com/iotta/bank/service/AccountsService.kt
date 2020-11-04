package com.iotta.bank.service

import com.iotta.bank.model.Account
import com.iotta.bank.model.AccountType
import com.iotta.bank.persistence.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface AccountsService {

    fun getAccount(iban: String): Account?
    fun deposit(iban: String, amount: Double): Boolean
    fun transfer(iban_from: String, iban_to: String, amount: Double): Boolean
    fun balance(iban: String): Double?

    @Service
    class Impl(private val repository: AccountRepository,
               private val validator: TransferValidator,
               private val transactionsService: TransactionsService
    ) : AccountsService {

        override fun getAccount(iban: String): Account? {
            return repository.getAccount(iban)
        }

        override fun deposit(iban: String, amount: Double): Boolean {
            if (amount <= 0) {
                return false
            }

            return repository.getAccount(iban)?.let {
                val transactionId = transactionsService.submitDepositTransaction(from = iban, amount = amount)
                val result = when (it.type) {
                    AccountType.CHECKING -> repository.updateBalance(iban, amount)
                    AccountType.SAVING -> repository.updateBalance(iban, amount)
                    // In case of Load we want to remove from load balance and not add
                    AccountType.LOAN -> if (amount > it.balance) false else repository.updateBalance(iban, -1 * amount)
                }
                transactionsService.finishTransaction(transactionId, result)
                return result
            } ?: false
        }

        override fun transfer(iban_from: String, iban_to: String, amount: Double): Boolean {
            repository.getAccount(iban_from)?.let { from ->
                repository.getAccount(iban_to)?.let { to ->
                    if (validator.validate(from, to, amount)) {
                        val transactionId = transactionsService.submitTransferTransaction(from.iban, to.iban, amount)

                        var result = repository.updateBalance(from.iban, -1 * amount)
                        if (!result) {
                            transactionsService.finishTransaction(transactionId, false)
                            return false
                        }

                        result = repository.updateBalance(to.iban, if (to.type == AccountType.LOAN) -1 * amount else amount)
                        if (!result) {
                            transactionsService.finishTransaction(transactionId, false)
                            // Return the money back because transfer failed at destination
                            result = repository.updateBalance(from.iban, amount)
                        }
                        if (!result) {
                            //TODO Transaction will stay in pending state, we need to take care of this situation later
                            return false
                        }
                        transactionsService.finishTransaction(transactionId, true)
                        return true
                    } else
                        return false
                } ?: return false
            } ?: return false


        }


        override fun balance(iban: String): Double? {
            return repository.getAccount(iban)?.balance
        }

    }
}
