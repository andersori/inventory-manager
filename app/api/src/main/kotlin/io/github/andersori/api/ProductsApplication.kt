package io.github.andersori.api

import io.github.andersori.utils.CustomLoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProductsApplication

val logger = CustomLoggerFactory.inline(ProductsApplication::class.java)

suspend fun main(args: Array<String>) {
	logger.info("Iniciando o APP")
	runApplication<ProductsApplication>(*args)
	logger.info("APP iniciado")
}
