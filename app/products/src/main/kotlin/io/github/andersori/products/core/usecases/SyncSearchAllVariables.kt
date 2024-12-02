package io.github.andersori.products.core.usecases

interface SyncSearchAllVariables {
    fun search(identifier: String, vararg keys: String): Map<String, *>
}