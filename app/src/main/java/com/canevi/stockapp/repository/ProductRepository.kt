package com.canevi.stockapp.repository

import android.content.Context
import com.canevi.stockapp.model.Category
import com.canevi.stockapp.model.Product
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

    suspend fun getProducts(): List<Product> = try {
        withContext(Dispatchers.IO) {
            api?.getProducts() ?: emptyList()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
    }

    suspend fun addProduct(product: Product): Product? = try {
        withContext(Dispatchers.IO) {
            api?.addProduct(product)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }

    suspend fun searchProducts(name: String): List<Product> = try {
        withContext(Dispatchers.IO) {
            api?.searchProducts(name) ?: emptyList()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
    }

    suspend fun getCategoriesOfProduct(productId: String): List<Category> = try {
        withContext(Dispatchers.IO) {
            api?.getCategoriesOfProduct(productId) ?: emptyList()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
    }

    suspend fun getImagesForProduct(productId: String): List<Map<String, String>> = try {
        withContext(Dispatchers.IO) {
            api?.getImagesForProduct(productId) ?: emptyList()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
    }

}