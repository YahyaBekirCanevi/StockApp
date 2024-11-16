package com.canevi.stockapp.repository

import android.content.Context
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.model.dto.ImageDTO
import com.canevi.stockapp.network.ApiClient
import com.canevi.stockapp.network.ProductAPI
import com.canevi.stockapp.util.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun getProducts(): List<Product> = try {
        withContext(Dispatchers.IO) {
            api?.getProducts() ?: emptyList()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }

    suspend fun addProduct(product: Product): Product? = try {
        withContext(Dispatchers.IO) {
            api?.addProduct(product)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun searchProducts(name: String): List<Product> = try {
        withContext(Dispatchers.IO) {
            api?.searchProducts(name) ?: emptyList()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }

    suspend fun getCategoriesOfProduct(productId: String): Map<String, String> = try {
        withContext(Dispatchers.IO) {
            api?.getCategoriesOfProduct(productId) ?: emptyMap()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }

    suspend fun getImagesForProduct(productId: String): List<ImageDTO> = try {
        withContext(Dispatchers.IO) {
            api?.getImagesForProduct(productId) ?: emptyList()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }

    suspend fun addImagesToProduct(productId: String, imageFiles: List<ImageDTO>): String = try {
        withContext(Dispatchers.IO) {
            api?.addImagesToProduct(productId, imageFiles) ?: ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

    suspend fun addCategoriesToProduct(productId: String, categories: List<String>): String = try {
        withContext(Dispatchers.IO) {
            api?.addCategoriesToProduct(productId, categories) ?: ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}