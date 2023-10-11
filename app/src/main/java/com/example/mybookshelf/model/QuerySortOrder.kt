package com.example.mybookshelf.model

enum class QuerySortOrder {
    ALPHABETICAL,
    ALPHABETICAL_REVERSE,
    LAST_UPDATED,
    LAST_UPDATED_REVERSE,
    LAST_ADDED,
    LAST_ADDED_REVERSE,
}

fun List<MyBook>.sort(
    sortOrder: QuerySortOrder
): List<MyBook> {
    return when (sortOrder) {
        QuerySortOrder.ALPHABETICAL -> sortedBy{it.title}
        QuerySortOrder.ALPHABETICAL_REVERSE -> sortedBy{it.title}
        QuerySortOrder.LAST_UPDATED -> sortedBy{it.lastUpdated}.reversed()
        QuerySortOrder.LAST_UPDATED_REVERSE -> sortedBy { it.lastUpdated }.reversed()
        QuerySortOrder.LAST_ADDED -> reversed()
        QuerySortOrder.LAST_ADDED_REVERSE -> reversed().reversed()
    }
}
