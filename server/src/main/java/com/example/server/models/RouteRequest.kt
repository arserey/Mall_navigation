package com.example.server.models

import kotlinx.serialization.Serializable

@Serializable
data class RouteRequest(
    val startX: Double,
    val startY: Double,
    val endX: Double,
    val endY: Double,
    val floor: Int
)