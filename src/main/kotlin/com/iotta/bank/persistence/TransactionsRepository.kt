package com.iotta.bank.persistence

import com.iotta.bank.model.Transaction
import com.iotta.bank.model.TransactionStatus
import com.iotta.bank.model.TransactionType
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap


interface TransactionsRepository {

    fun getTransactions(iban: String): List<Transaction>
    fun getTransactionsByType(iban: String?, type: TransactionType): List<Transaction>
    fun submitTransaction(from: String, type: TransactionType, amount: Double, to: String): String
    fun updateTransaction(id: String, state: TransactionStatus): Boolean

    @Repository
    class Impl : TransactionsRepository {

        private final val transactions: ConcurrentHashMap<String, Transaction> = ConcurrentHashMap()

        override fun getTransactions(iban: String): List<Transaction> {
            return transactions.filter { it.key == iban }.values.toList()
        }

        override fun getTransactionsByType(iban: String?, type: TransactionType): List<Transaction> {
            return iban?.let { transactions.filter { it.key == iban && it.value.type == type }.values.toList() }
                    ?: transactions.filter { it.value.type == type }.values.toList()
        }

        override fun submitTransaction(from: String, type: TransactionType, amount: Double, to: String): String {
            val id = UUID.randomUUID().toString()
            transactions[id] = Transaction(
                    reference = id,
                    amount = amount,
                    destination = to,
                    date = LocalDateTime.now(),
                    iban = from,
                    type = type,
                    status = TransactionStatus.Pending,
                    description = "Submitted"
            )
            return id
        }

        override fun updateTransaction(id: String, state: TransactionStatus): Boolean {
            return transactions[id]?.let {
                transactions[id] = it.copy(status = state)
                true
            } ?: false
        }

    }
}
