package io.github.andersori.core.exceptions

import io.github.andersori.core.exceptions.base.BaseException

class AccountNotFound(message: String) : BaseException(message) {
    override fun type(): String = "account_not_found"
}