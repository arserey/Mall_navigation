package com.example.server.models

import kotlinx.serialization.Serializable

@Serializable
data class Shop(
    val id: String,
    val name: String,
    val floor: Int,
    val x: Double,
    val y: Double,
    val category: String
)