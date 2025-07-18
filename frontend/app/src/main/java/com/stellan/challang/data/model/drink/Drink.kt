package com.stellan.challang.data.model.drink

data class Drink(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val tastingNote: String,
    val origin: String,
    val minAbv: Double,
    val maxAbv: Double,
    val levelId: Int,
    val levelName: String,
    val typeId: Int,
    val typeName: String,
    val liquorTags: List<LiquorTag>,
    val averageRating: Double,
    val topTagStats: List<TopTagStat>
)

data class LiquorTag(
    val id: Int,
    val tagId: Int,
    val tagName: String,
    val isCore: Boolean
)

data class TopTagStat(
    val tagName: String,
    val percentage: Double
)
