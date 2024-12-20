package io.github.andersori.core.v1.usecases.variables

import io.github.andersori.core.v1.ports.out.AccountInformation
import io.github.andersori.core.v1.usecases.variables.unique.FindActiveAccount
import io.github.andersori.core.v1.usecases.variables.unique.FindFakeAccount
import io.github.andersori.core.v1.usecases.variables.unique.FindTestAccount
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MappedVariablesBasedOnAccountTest {
    private lateinit var accountInformation: AccountInformation
    private lateinit var mappedVariablesBasedOnAccount: MappedVariablesBasedOnAccount

    @BeforeEach
    fun before() {
        accountInformation = mock<AccountInformation>()
        mappedVariablesBasedOnAccount = MappedVariablesBasedOnAccount(
            accountInformation,
            FindTestAccount(),
            FindActiveAccount(),
            FindFakeAccount()
        )
    }

    @Test
    fun `test getNewRoot`() {
        assertDoesNotThrow { mappedVariablesBasedOnAccount.getNewRoot() }
            .also { assertTrue(it is io.github.andersori.core.v1.usecases.variables.unique.FindAccount) }
    }

    @Test
    fun `variaveis mapeadas`() {
        val prop = MappedVariables::class.declaredMemberProperties.first { it.name == "mappedVars" }
        prop.isAccessible = true
        val mappedVars = prop.get(mappedVariablesBasedOnAccount) as Map<*, *>

        assertEquals(setOf("TEST_ACCOUNT", "ACTIVE_ACCOUNT", "FAKE_ACCOUNT"), mappedVars.keys)
    }
}