package com.canevi.stockapp.network

import com.canevi.stockapp.model.Product
import com.canevi.stockapp.model.dto.DetailedProductDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductAPI {
    @GET("product")
    suspend fun getProducts(): List<Product>

    @POST("product")
    suspend fun addProduct(@Body product: Product): Product

    @DELETE("product/{productId}")
    suspend fun deleteProduct(@Path("productId") name: String): List<Product>

    @GET("product/search")
    suspend fun searchProducts(@Query("name") name: String): List<Product>

    @GET("product/detail/{productId}")
    suspend fun getProductDetail(@Path("productId") productId: String): DetailedProductDTO
}