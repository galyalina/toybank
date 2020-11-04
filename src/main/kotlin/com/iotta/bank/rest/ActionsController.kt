package com.iotta.bank.rest

import com.iotta.bank.service.AccountsService
import com.iotta.bank.service.TransactionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong


@RestController
class ActionsController {
    private val counter = AtomicLong()

    @Autowired
    private lateinit var accountsService: AccountsService

    @Autowired
    private lateinit var transactionsService: TransactionsService

    @RequestMapping("/")
    fun index(): String? {
        return "Greetings from small private Bank code challenge!"
    }

    @Throws(Exception::class)
    @GetMapping("/deposit")
    fun deposit(@RequestParam(value = "IBAN") iban: String, @RequestParam(value = "amount") amount: Double): ActionResult {
        return if (accountsService.deposit(iban, amount))
            ActionResult(counter.incrementAndGet(), template + "Succeeded")
        else
            ActionResult(counter.incrementAndGet(), template + "Failed")
    }

    @Throws(Exception::class)
    @GetMapping("/transfer")
    fun transfer(@RequestParam(value = "IBAN") from: String, @RequestParam(value = "to") to: String,
                 @RequestParam(value = "amount") amount: Double): ActionResult {
        return if (accountsService.transfer(from, to, amount))
            ActionResult(counter.incrementAndGet(), template + "Succeeded")
        else
            ActionResult(counter.incrementAndGet(), template + "Failed")
    }

    @Throws(Exception::class)
    @GetMapping("/showBalance")
    fun showBalance(@RequestParam(value = "IBAN") iban: String): ActionResult {
        return ActionResult(counter.incrementAndGet(), "" + accountsService.balance(iban))
    }

    @Throws(Exception::class)
    @GetMapping("/history")
    fun history(@RequestParam(value = "IBAN") iban: String): ActionResult {
        return ActionResult(counter.incrementAndGet(), " " + transactionsService.getTransactions(iban))
    }

    companion object {
        private const val template = "Request result, "
    }

}
