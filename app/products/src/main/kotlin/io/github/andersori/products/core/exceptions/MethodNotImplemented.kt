package io.github.andersori.products.core.exceptions

class MethodNotImplemented(message: String) : BaseException(message) {
    override fun type(): String = "method_not_implemented"
}