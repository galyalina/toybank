package com.iotta.bank.rest

import com.iotta.bank.service.AccountsService
import com.iotta.bank.service.TransactionsService
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.AssertionErrors.assertEquals
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@ExtendWith(SpringExtension::class)
@WebMvcTest(ActionsController::class)
internal class ActionsControllerTest {

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun accountService() = mockk<AccountsService>()

        @Bean
        fun transactionsService() = mockk<TransactionsService>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Spring context loaded`() {
    }

    @Test
    fun `Default message`() {
        val expectedMessage = "Greetings from small private Bank code challenge!"
        val result = mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andDo(print())
                .andReturn()

        assertEquals("Check result message", expectedMessage, result.response.contentAsString)
    }
}
