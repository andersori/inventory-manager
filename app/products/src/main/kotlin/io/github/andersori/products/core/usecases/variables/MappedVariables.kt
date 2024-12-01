package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder

abstract class MappedVariables<E, T>(private val vars: Map<String, Finder<*, *>>) {

    companion object {
        fun <E, T> getDefaultFinder() = object : Finder<E, T>() {}
    }

    fun configRootHandler(vararg keys: String): Finder<E, T> {
        val root = getNewRoot()

        val foundKeys = keys.associate { key ->
            addHandlers(vars[key.uppercase()], key, root)
        }.filterValues { it != null }

        if (foundKeys.isEmpty()) {
            return getDefaultFinder()
        }

        return root
    }

    abstract fun getNewRoot(): Finder<E, T>

    abstract fun addHandlers(
        finder: Finder<*, *>?,
        key: String,
        root: Finder<E, T>
    ): Pair<String, Finder<E, T>?>
}