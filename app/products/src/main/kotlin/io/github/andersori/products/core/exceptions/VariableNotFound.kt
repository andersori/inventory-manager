package io.github.andersori.products.core.exceptions

class VariableNotFound(message: String) : RuntimeException(message), ErrorType {
    override fun type(): String = "unmapped_variable"
}