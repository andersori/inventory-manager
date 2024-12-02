package io.github.andersori.products.core.exceptions

class InvalidSPG(message: String) : BaseException(message) {
    override fun type(): String = "invalid_spg"
}