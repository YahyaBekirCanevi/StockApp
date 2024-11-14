package com.canevi.stockapp.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonUnquotedLiteral
import java.math.BigDecimal

typealias BigDecimalJson = @Serializable(with = BigDecimalSerializer::class) BigDecimal

object BigDecimalSerializer : KSerializer<BigDecimal> {

    override val descriptor = PrimitiveSerialDescriptor("java.math.BigDecimal", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): BigDecimal =
        decoder.decodeDouble().toBigDecimal()

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: BigDecimal) = when(encoder) {
        is JsonEncoder -> encoder.encodeJsonElement(JsonUnquotedLiteral(value.toPlainString()))
        else -> encoder.encodeDouble(value.toDouble())
    }
}