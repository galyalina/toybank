package com.iotta.bank.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Transaction(
        val reference: String,
        @JsonProperty("iban")
        val iban: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.046'Z'")
        val date: LocalDateTime,
        val amount: Double,
        val description: String?,
        val destination: String,
        val status: TransactionStatus,
        val type: TransactionType)
