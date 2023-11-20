package com.example.mybookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NytListOverview(
    @SerialName(value = "num_results")
    val totalItems: Int,
    val results: NytListPublication
)

@Serializable
data class NytListPublication(
    @SerialName(value = "bestsellers_date")
    val publicationDate: String,
    val lists: List<NytPublishedList>
)

@Serializable
data class NytPublishedList(
    @SerialName(value = "list_name")
    val listName: String,
    val books: List<Bestseller>
)

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

@Serializable
data class NytListSearch(
    val results: List<NytBestsellerList>?
)
@Serializable
data class NytBestsellerList(
    @SerialName(value = "list_name")
    val listName: String,
    @SerialName(value = "list_name_encoded")
    val listLocation: String,
    @SerialName(value = "newest_published_date")
    val publishedDate: String,
    @SerialName(value = "oldest_published_date")
    val firstPublished: String,
    @SerialName(value = "updated")
    val frequency: String,
)
