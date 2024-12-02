package io.github.andersori.products.core.exceptions

class UnnecessaryExecution(message: String) : BaseException(message) {
    override fun type(): String = "unnecessary_execution"
}