package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.exceptions.UnnecessaryExecution
import io.github.andersori.products.core.exceptions.VariableNotFound

abstract class MappedVariables<E, T>(private val mappedVars: Map<String, Finder<T, *>>) {

    companion object {
        fun <E, T> getDefaultFinder(rootKey: String) = object : Finder<E, T>(rootKey) {
            override fun execute(identifier: E): T {
                throw UnnecessaryExecution("for key = ${key()}")
            }
        }
    }

    fun configRootHandler(vararg vars: String): Finder<E, T> {
        return configRootHandler(
            ignoreUnknownVar = true,
            vars = vars
        )
    }

    fun configRootHandler(ignoreUnknownVar: Boolean, vararg vars: String): Finder<E, T> {
        val root = getNewRoot()

        val foundKeys = vars.associate { key ->
            addHandlers(mappedVars[key.uppercase()], key, ignoreUnknownVar, root)
        }.filterValues { it != null }

        if (foundKeys.isEmpty()) {
            return getDefaultFinder(root.key())
        }

        return root
    }

    abstract fun getNewRoot(): Finder<E, T>

    private fun addHandlers(
        finder: Finder<T, *>?,
        key: String,
        ignoreUnknownVar: Boolean,
        root: Finder<E, T>
    ): Pair<String, Finder<E, T>?> {
        return if (finder != null) {
            key to root.addNext(finder)
        } else {
            if (ignoreUnknownVar) {
                key to null
            } else {
                throw VariableNotFound("var $key not found")
            }
        }
    }
}