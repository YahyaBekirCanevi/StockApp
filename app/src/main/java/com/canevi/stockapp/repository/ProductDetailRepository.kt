package com.canevi.stockapp.repository

import android.content.Context
import com.canevi.stockapp.model.dto.ProductDTO
import com.canevi.stockapp.network.ApiClient
import com.canevi.stockapp.network.ProductDetailAPI
import com.canevi.stockapp.util.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductDetailRepository(context: Context) {
    private var api: ProductDetailAPI? = null

    init {
        api = try {
            ApiClient.getClient(Environment.BASE_URL, context)?.create(ProductDetailAPI::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun readProduct(productId: String): ProductDTO? = try {
        withContext(Dispatchers.IO) {
            api?.readProduct(productId)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun createProduct(product: ProductDTO): String = try {
        withContext(Dispatchers.IO) {
            api?.createProduct(product) ?: ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

    suspend fun deleteProduct(productId: String): String = try {
        withContext(Dispatchers.IO) {
            api?.deleteProduct(productId) ?: ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}