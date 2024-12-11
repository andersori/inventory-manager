package io.github.andersori.core.v1.exceptions

import io.github.andersori.core.v1.exceptions.base.BaseException

class ApiRequestException(message: String) : BaseException(message) {
    override fun type(): String = "api_request"
}