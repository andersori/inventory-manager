package io.github.andersori.products.core.usecases

import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.usecases.variables.unique.FindClient
import io.github.andersori.products.core.usecases.variables.unique.FindSPG
import io.github.andersori.products.core.usecases.variables.unique.FindStates
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FindClientTest {
    private lateinit var findClient: FindClient

    @BeforeEach
    fun before() {
        findClient = FindClient()
    }

    @Test
    fun `testando o valor padrao`() {
        assertEquals(
            Client(
                cpf = "000.000.000-00"
            ), findClient.execute("a")
        )
    }

    @Test
    fun `testando next`() {
        val nextHandler1 = FindSPG()
        val nextHanderr2 = FindStates()

        findClient.addNext(nextHandler1)
        findClient.addNext(nextHanderr2)

        println(findClient.handler("1234"))
    }
}