package com.example.server.models

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val x: Double,
    val y: Double,
    val floor: Int
)