package com.iotta.bank.service

import com.iotta.bank.model.Transaction
import com.iotta.bank.model.TransactionStatus
import com.iotta.bank.model.TransactionType
import com.iotta.bank.persistence.TransactionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


interface TransactionsService {

    fun submitTransferTransaction(from: String, to: String, amount: Double): String
    fun submitDepositTransaction(from: String, amount: Double): String
    fun getTransactions(iban: String): List<Transaction>
    fun finishTransaction(transactionId: String, succeeded: Boolean): Boolean

    @Service
    class Impl() : TransactionsService {

        @Autowired
        private lateinit var repository: TransactionsRepository

        override fun submitTransferTransaction(from: String, to: String, amount: Double): String {
            return repository.submitTransaction(from = from, to = to, amount = amount, type = TransactionType.TRANSFER)
        }

        override fun submitDepositTransaction(from: String, amount: Double): String {
            return repository.submitTransaction(from = from, to = from, amount = amount, type = TransactionType.DEPOSIT)
        }

        override fun getTransactions(iban: String): List<Transaction> {
            return repository.getTransactions(iban)
        }

        override fun finishTransaction(transactionId: String, succeeded: Boolean): Boolean {
            return repository.updateTransaction(transactionId, if (succeeded) TransactionStatus.Success else TransactionStatus.Failed)
        }
    }
}
