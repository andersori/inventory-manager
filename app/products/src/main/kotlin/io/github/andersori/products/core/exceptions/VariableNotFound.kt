package io.github.andersori.products.core.exceptions

class VariableNotFound(message: String) : BaseException(message) {
    override fun type(): String = "unmapped_variable"
}