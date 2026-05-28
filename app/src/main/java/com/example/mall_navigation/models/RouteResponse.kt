package com.example.mall_navigation.models

data class RouteResponse(
    val points: List<Point>,
    val distance: Double,
    val instructions: List<String>
)