package com.canevi.stockapp.repository

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.model.dto.DetailedProductDTO
import com.canevi.stockapp.network.ApiClient
import com.canevi.stockapp.network.ProductAPI
import com.canevi.stockapp.util.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ProductRepository(context: Context) {
    private var api: ProductAPI? = null

    init {
        api = try {
            ApiClient.getClient(Environment.BASE_URL, context)?.create(ProductAPI::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getProducts(): List<Product> {
        return try {
            withContext(Dispatchers.IO) {
                api?.getProducts() ?: emptyList()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addProduct(product: Product): Product? {
        return try {
            withContext(Dispatchers.IO) {
                api?.addProduct(product)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    suspend fun searchProducts(name: String): List<Product> {
        return try {
            withContext(Dispatchers.IO) {
                api?.searchProducts(name) ?: emptyList()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getProductDetail(productId: String): DetailedProductDTO? {
        return try {
            withContext(Dispatchers.IO) {
                api?.getProductDetail(productId)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}