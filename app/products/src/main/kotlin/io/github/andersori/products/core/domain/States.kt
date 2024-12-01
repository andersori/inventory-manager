package io.github.andersori.products.core.domain

import io.github.andersori.products.core.exceptions.StateNotFound

enum class States(val id: Int, val names: List<String>) {
    NUMBER_3(3, listOf("Ceará", "Maranhão", "Piauí"));


    companion object {
        @Throws(StateNotFound::class)
        fun getSender(id: Int): States {
            return States.entries.firstOrNull { it.id == id } ?: throw StateNotFound("Identificador $id não encontrado")
        }
    }
}