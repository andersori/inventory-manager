package io.github.andersori.core.v2

interface Next<Result, NextType> {
    fun addNext(handler: NextType): Next<Result, NextType>
    suspend fun runAllNext(identifier: Result, ignoreError: Boolean): Map<String, Any>
}