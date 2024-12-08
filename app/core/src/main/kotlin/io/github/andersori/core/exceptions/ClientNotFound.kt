package io.github.andersori.core.exceptions

import io.github.andersori.core.exceptions.base.BaseException

class ClientNotFound(message: String) : BaseException(message) {
    override fun type(): String = "client_not_found"
}