package com.canevi.stockapp.network

import com.canevi.stockapp.model.dto.ProductDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductOrchestrationAPI {
    @GET("product/api/{productId}")
    suspend fun readProduct(@Path("productId") productId: String): ProductDTO

    @POST("product/api")
    suspend fun createProduct(@Body product: ProductDTO): ProductDTO

    @DELETE("product/api/{productId}")
    suspend fun deleteProduct(@Path("productId") productId: String): String
}