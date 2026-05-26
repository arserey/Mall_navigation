package com.example.mall_navigation.models

data class RouteResponse (
    val id: String,
    val name: String,
    val floor: Int,
    val x: Double,
    val y: Double,
    val category: String
)