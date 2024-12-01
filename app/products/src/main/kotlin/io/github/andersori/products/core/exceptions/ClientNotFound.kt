package io.github.andersori.products.core.exceptions

class ClientNotFound(message: String) : BaseException(message) {
    override fun type(): String = "client_information"
}