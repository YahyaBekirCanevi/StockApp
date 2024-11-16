package com.canevi.stockapp.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    val id: String? = null,
    val name: String,
    val description: String,
    val price: Double,
    val categories: List<String> = mutableListOf(),
    val images: List<ImageDTO> = mutableListOf()
)