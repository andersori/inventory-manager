package io.github.andersori.products.core.exceptions

abstract class BaseException(override val message: String) : RuntimeException(message), ErrorType