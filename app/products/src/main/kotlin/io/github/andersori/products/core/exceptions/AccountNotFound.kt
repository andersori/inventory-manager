package io.github.andersori.products.core.exceptions

class AccountNotFound(message: String) : BaseException(message) {
    override fun type(): String = "account_information"
}