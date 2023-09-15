package com.example.mybookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class BestsellerSearch(
    @SerialName(value = "num_results")
    val totalItems: Int,
    val results: BestsellerResults
)

@Serializable
data class BestsellerResults(
    @SerialName(value = "list_name")
    val listName: String,
    @SerialName(value = "published_date")
    val publishedDate: String,
    @SerialName(value = "published_date_description")
    val publishedDescription: String,
    @SerialName(value = "books")
    val bestsellerList: List<Bestseller>
)
