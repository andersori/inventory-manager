package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.ports.out.ClientInformation
import io.github.andersori.products.core.usecases.variables.unique.FindClient
import io.github.andersori.products.core.usecases.variables.unique.FindSPG
import io.github.andersori.products.core.usecases.variables.unique.FindStates
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MappedVariablesBasedOnClientTest {
    private lateinit var clientInformation: ClientInformation
    private lateinit var mappedVariablesBasedOnClient: MappedVariablesBasedOnClient

    @BeforeEach
    fun before() {
        clientInformation = mock<ClientInformation>()
        mappedVariablesBasedOnClient = MappedVariablesBasedOnClient(
            clientInformation,
            FindSPG(),
            FindStates()
        )
    }

    @Test
    fun `test getNewRoot`() {
        assertDoesNotThrow { mappedVariablesBasedOnClient.getNewRoot() }
            .also { assertTrue(it is FindClient) }
    }

    @Test
    fun `variaveis mapeadas`() {
        val prop = MappedVariables::class.declaredMemberProperties.first { it.name == "mappedVars" }
        prop.isAccessible = true
        val mappedVars = prop.get(mappedVariablesBasedOnClient) as Map<*, *>

        assertEquals(setOf("SPG", "STATES"), mappedVars.keys)
    }
}