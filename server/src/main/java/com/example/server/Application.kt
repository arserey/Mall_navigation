package com.example.server

import com.example.server.models.RouteRequest
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            anyHost()
            allowHeader(HttpHeaders.ContentType)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Get)
        }

        val navigationService = NavigationService()

        routing {
            get("/shops") {
                call.respond(navigationService.getAllShops())
            }
            get("/shops/floor/{floor}") {
                val floor = call.parameters["floor"]?.toIntOrNull() ?: 1
                call.respond(navigationService.getShopsByFloor(floor))
            }
            get("/shops/search") {
                val query = call.request.queryParameters["q"] ?: ""
                call.respond(navigationService.searchShops(query))
            }
            post("/route") {
                val request = call.receive<RouteRequest>()
                val response = navigationService.findPath(request)
                call.respond(response)
            }
        }
    }.start(wait = true)
}