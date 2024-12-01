package io.github.andersori.products.core.exceptions

class ClientNotFound(message: String) : RuntimeException(message), ErrorType {
    override fun type(): String = "client_information"
}