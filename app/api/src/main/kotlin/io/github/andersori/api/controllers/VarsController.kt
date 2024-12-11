package io.github.andersori.api.controllers

import io.github.andersori.api.controllers.dto.Response
import io.github.andersori.api.controllers.dto.ResponseData
import io.github.andersori.core.v1.ports.`in`.SearchVariables
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureNanoTime

@RestController("/")
class VarsController(
    private val searchVariables: SearchVariables
) {
    @GetMapping("/vars/{id}")
    suspend fun get(
        @PathVariable id: String,
        @RequestParam vars: Set<String> = emptySet()
    ): ResponseEntity<Response> {
        val res: Map<String, *>
        val elapsedTime = measureNanoTime {
            res = searchVariables.search(
                identifier = id,
                keys = vars.toTypedArray()
            )
        }
        println("Tempo de execução: ${elapsedTime / 1_000_000}ms")
        return ResponseEntity.ok().body(
            Response(
                data = ResponseData(
                    vars = res
                )
            )
        )
    }
}