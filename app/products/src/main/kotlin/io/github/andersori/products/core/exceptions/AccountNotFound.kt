package io.github.andersori.products.core.exceptions

class AccountNotFound(message: String) : RuntimeException(message), ErrorType {
    override fun type(): String = "account_information"
}