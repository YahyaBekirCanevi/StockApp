package com.canevi.stockapp.repository

import android.content.Context
import com.canevi.stockapp.model.dto.ProductDTO
import com.canevi.stockapp.network.ApiClient
import com.canevi.stockapp.network.ProductOrchestrationAPI
import com.canevi.stockapp.util.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ProductOrchestrationRepository(context: Context) {
    private var api: ProductOrchestrationAPI? = null

    init {
        api = try {
            ApiClient.getClient(Environment.BASE_URL, context)?.create(ProductOrchestrationAPI::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun readProduct(productId: String): ProductDTO? = try {
        withContext(Dispatchers.IO) {
            api?.readProduct(productId)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }

    suspend fun createProduct(product: ProductDTO): ProductDTO? = try {
        withContext(Dispatchers.IO) {
            api?.createProduct(product)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }

    suspend fun deleteProduct(productId: String): String = try {
        withContext(Dispatchers.IO) {
            api?.deleteProduct(productId) ?: ""
        }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}