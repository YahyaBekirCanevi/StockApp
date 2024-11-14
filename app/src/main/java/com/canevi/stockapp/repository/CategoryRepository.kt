package com.canevi.stockapp.repository

import android.content.Context
import com.canevi.stockapp.model.Category
import com.canevi.stockapp.network.ApiClient
import com.canevi.stockapp.network.CategoryAPI
import com.canevi.stockapp.util.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(context: Context) {
    private var api: CategoryAPI? = null

    init {
        api = try {
            ApiClient.getClient(Environment.BASE_URL, context)?.create(CategoryAPI::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCategories(): List<Category> = try {
        withContext(Dispatchers.IO) {
            api?.getCategories() ?: emptyList()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
    suspend fun addPCategory(category: Category): Category? = try {
        withContext(Dispatchers.IO) {
            api?.addPCategory(category)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    suspend fun searchCategories(name: String): List<Category> = try {
        withContext(Dispatchers.IO) {
            api?.searchCategories(name) ?: emptyList()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
    suspend fun deleteCategory(id: String): Boolean = try {
        withContext(Dispatchers.IO) {
            api?.deleteCategory(id)
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}