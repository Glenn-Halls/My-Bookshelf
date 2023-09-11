package com.example.mybookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bestseller(
    @SerialName(value = "uri")
    val nytLink: String
)
