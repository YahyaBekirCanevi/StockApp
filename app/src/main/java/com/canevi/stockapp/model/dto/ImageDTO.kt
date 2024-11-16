package com.canevi.stockapp.model.dto

import kotlinx.serialization.Serializable
import java.util.Base64

@Serializable
data class ImageDTO(
    val id: String? = null,
    val productId: String,
    val imageData: String
) {
    fun getImage(): ByteArray {
        return Base64.getDecoder().decode(imageData)
    }

    companion object {
        fun encodeToString(imageData: ByteArray): String {
            return Base64.getEncoder().encodeToString(imageData)
        }
        fun decode(buffer: String): ByteArray {
            return Base64.getDecoder().decode(buffer)
        }
        fun ofProduct(productId: String, imageData: ByteArray): ImageDTO = ImageDTO(
            productId = productId,
            imageData = encodeToString(imageData)
        )
    }
}