package io.github.andersori.core.v1.exceptions

import io.github.andersori.core.v1.exceptions.base.BaseException

class ClientNotFound(message: String) : BaseException(message) {
    override fun type(): String = "client_not_found"
}