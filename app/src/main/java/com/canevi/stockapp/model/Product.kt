package com.canevi.stockapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String? = null,
    val name: String,
    val description: String,
    val price: Double,
    val categoryIdList: MutableList<String> = mutableListOf()
)