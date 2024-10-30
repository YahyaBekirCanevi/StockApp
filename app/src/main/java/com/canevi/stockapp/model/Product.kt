package com.canevi.stockapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String? = null,
    val name: String,
    val quantity: Int
)