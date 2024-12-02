package io.github.andersori.products.core.exceptions

class InvalidStates(message: String) : BaseException(message) {
    override fun type(): String = "invalid_states"
}