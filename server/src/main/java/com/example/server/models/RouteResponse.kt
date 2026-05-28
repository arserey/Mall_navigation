package com.example.server.models

import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    val points: List<Point>,
    val distance: Double,
    val instructions: List<String>
)