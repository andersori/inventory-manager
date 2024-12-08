package io.github.andersori.core.exceptions

import io.github.andersori.core.exceptions.base.BaseException

class ApiRequestException(message: String) : BaseException(message) {
    override fun type(): String = "api_request"
}