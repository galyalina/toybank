package com.iotta.bank.model

data class Account(val iban: String, val type: AccountType, val allowedTransfers: List<String>, val balance: Double = 0.0)
