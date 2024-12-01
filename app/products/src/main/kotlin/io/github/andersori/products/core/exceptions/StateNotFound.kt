package io.github.andersori.products.core.exceptions

class StateNotFound(message: String) : RuntimeException(message), ErrorType {
    override fun type(): String = "enum_states"
}