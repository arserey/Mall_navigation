package com.example.mall_navigation.network

import com.example.mall_navigation.models.RouteRequest
import com.example.mall_navigation.models.RouteResponse
import com.example.mall_navigation.models.Shop
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

class ApiService {
    @GET("shops")
    suspend fun getAllShops(): List<Shop>

    @GET("shops/floor/{floor}")
    suspend fun getShopsByFloor(@Path("floor") floor: Int): List<Shop>

    @GET("shops/search")
    suspend fun searchShops(@Query("q") query: String): List<Shop>

    @POST("route")
    suspend fun getRoute(@Body request: RouteRequest): RouteResponse
}