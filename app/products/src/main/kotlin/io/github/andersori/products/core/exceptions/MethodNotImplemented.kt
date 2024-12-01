package io.github.andersori.products.core.exceptions

class MethodNotImplemented(message: String) : RuntimeException(message), ErrorType {
    override fun type(): String = "method_not_implemented"
}