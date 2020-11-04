package com.iotta.bank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ToybankApplication

fun main(args: Array<String>) {
    runApplication<ToybankApplication>(*args)
}
