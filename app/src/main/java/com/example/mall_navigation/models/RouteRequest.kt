package com.example.mall_navigation.models

class RouteRequest (
    val id: String,
    val name: String,
    val floor: Int,
    val x: Double,
    val y: Double,
    val category: String,
    val endX: Double,
    val startX: Double,
    val startY: Double,
    val endY: Double
)