package com.canevi.stockapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String? = null,
    val name: String
)