package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.exceptions.UnnecessaryExecution
import io.github.andersori.products.core.exceptions.VariableNotFound
import io.github.andersori.products.core.usecases.variables.MappedVariables.Companion.getDefaultFinder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MappedVariablesTest {
    private val root = getDefaultFinder<String, String>("test")

    private val testObject = object : MappedVariables<String, String>(
        mappedVars = mapOf(
            "KEYINT" to getDefaultFinder<String, Int>("test"),
            "KEYDOUBLE" to getDefaultFinder<String, Double>("test"),
            "KEYFLOAT" to getDefaultFinder<String, Float>("test")
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
            .also { assertNotEquals(root, it) }
            .also {
                assertThrows<UnnecessaryExecution> { it.execute("...") }
                    .also {
                        assertEquals("unnecessary_execution", it.type())
                        assertEquals("for key = TEST", it.message)
                    }
            }
    }

    @Test
    fun `test configRootHandler without ignoring Unknown Variable`() {
        assertThrows<VariableNotFound> { testObject.configRootHandler(false, "a") }
            .also {
                assertEquals("unmapped_variable", it.type())
                assertEquals("var a not found", it.message)
            }
    }
}