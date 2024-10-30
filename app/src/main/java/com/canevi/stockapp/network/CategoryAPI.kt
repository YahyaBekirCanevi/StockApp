package com.canevi.stockapp.network

import com.canevi.stockapp.model.Category
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryAPI {
    @GET("category")
    suspend fun getCategories(): List<Category>

    @POST("category")
    suspend fun addPCategory(@Body category: Category): Category

    @GET("category/search")
    suspend fun searchCategories(@Query("name") name: String): List<Category>

    @DELETE("category/{categoryId}")
    suspend fun deleteCategory(@Path("categoryId") id: String): Boolean
}