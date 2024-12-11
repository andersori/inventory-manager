package io.github.andersori.core.v1.exceptions

import io.github.andersori.core.v1.exceptions.base.BaseException

class AccountNotFound(message: String) : BaseException(message) {
    override fun type(): String = "account_not_found"
}