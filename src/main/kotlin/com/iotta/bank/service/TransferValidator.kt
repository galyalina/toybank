package com.iotta.bank.service

import com.iotta.bank.model.Account
import com.iotta.bank.model.AccountType
import org.springframework.stereotype.Component

interface TransferValidator {

    fun validate(from: Account, to: Account, amount: Double): Boolean

    @Component
    class Impl : TransferValidator {
        override fun validate(from: Account, to: Account, amount: Double): Boolean {
            return when (from.type) {
                AccountType.CHECKING -> from.balance >= amount
                AccountType.SAVING -> from.allowedTransfers.contains(to.iban) && from.balance >= amount
                AccountType.LOAN -> false
            }
        }

    }
}
