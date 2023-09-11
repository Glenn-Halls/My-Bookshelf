package com.example.mybookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BestsellerResult(
    @SerialName(value = "num_results")
    val totalItems: Int,
    val results: List<Bestseller>
)
