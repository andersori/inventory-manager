package io.github.andersori.products.core.usecases

interface SyncSearchAllVariables {
    fun syncSearch(identifier: String, vararg keys: String): Map<String, *>
}