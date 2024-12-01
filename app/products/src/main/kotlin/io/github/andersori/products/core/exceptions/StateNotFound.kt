package io.github.andersori.products.core.exceptions

class StateNotFound(message: String) : BaseException(message) {
    override fun type(): String = "enum_states"
}