package com.iotta.bank.rest.exceptions

sealed class ActionException(message: String) : RuntimeException(message) {
    class ActionNotFoundException(iban: String) : ActionException("Account $iban not found")
    class ActionGeneralException(message: String?) : ActionException("Something unexpected happened, please try later, some details:  $message")
}
