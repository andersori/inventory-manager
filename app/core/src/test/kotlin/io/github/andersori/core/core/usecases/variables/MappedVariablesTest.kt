package io.github.andersori.core.core.usecases.variables

import io.github.andersori.core.exceptions.VariableNotFound
import io.github.andersori.core.usecases.variables.MappedVariables
import io.github.andersori.core.usecases.variables.unique.Finder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class MappedVariablesTest {
    private val root = object : Finder<String, String>("test") {
        override suspend fun execute(identifier: String): String = "0"
    }

    private val testObject = object : MappedVariables<String, String>(
        mappedVars = mapOf(
            "KEYINT" to object : Finder<String, Int>("test") {
                override suspend fun execute(identifier: String): Int = 0
            },
            "KEYDOUBLE" to object : Finder<String, Int>("test") {
                override suspend fun execute(identifier: String): Int = 0
            },
            "KEYFLOAT" to object : Finder<String, Int>("test") {
                override suspend fun execute(identifier: String): Int = 0
            }
        )
    ) {
        override fun getNewRoot(): Finder<String, String> = root
    }

    @Test
    fun `test getNewRoot`() {
        val root1 = testObject.getNewRoot()
        val root2 = testObject.getNewRoot()
        assertEquals(root1, root2)
    }

    @Test
    fun `test configRootHandler with keys found`() {
        assertDoesNotThrow { testObject.configRootHandler("keyInt", "keyDouble", "keyFloat") }
            .also { assertEquals(root, it) }
    }

    @Test
    fun `test configRootHandler with keys not found`() {
        assertDoesNotThrow { testObject.configRootHandler("a", "b", "c") }
            .also { assertNull(it) }
    }

    @Test
    fun `test configRootHandler without ignoring Unknown Variable`() {
        assertThrows<VariableNotFound> { testObject.configRootHandler(false, "a") }
            .also {
                assertEquals("variable_not_found", it.type())
                assertEquals("var 'a' not found", it.message)
            }
    }
}