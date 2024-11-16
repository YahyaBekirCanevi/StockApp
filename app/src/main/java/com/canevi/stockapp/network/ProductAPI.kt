package com.canevi.stockapp.network

import com.canevi.stockapp.model.Product
import com.canevi.stockapp.model.dto.ImageDTO
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
    suspend fun deleteProduct(@Path("productId") productId: String): List<Product>

    @GET("product/search")
    suspend fun searchProducts(@Query("name") name: String): List<Product>

    @GET("product/{productId}/category")
    suspend fun getCategoriesOfProduct(@Path("productId") productId: String): Map<String, String>

    @GET("product/{productId}/images")
    suspend fun getImagesForProduct(@Path("productId") productId: String): List<ImageDTO>

    @POST("product/{productId}/images")
    suspend fun addImagesToProduct(@Path("productId") productId: String, @Body imageFiles: List<ImageDTO>): String

    @POST("product/{productId}/category")
    suspend fun addCategoriesToProduct(@Path("productId") productId: String, @Body categories: List<String>): String
}