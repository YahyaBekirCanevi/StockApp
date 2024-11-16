package com.canevi.stockapp.network

import com.canevi.stockapp.model.dto.ProductDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductDetailAPI {
    @GET("productDetail/{productId}")
    suspend fun readProduct(@Path("productId") productId: String): ProductDTO

    @POST("productDetail")
    suspend fun createProduct(@Body productDTO: ProductDTO): String

    @DELETE("productDetail/{productId}")
    suspend fun deleteProduct(@Path("productId") productId: String): String
}