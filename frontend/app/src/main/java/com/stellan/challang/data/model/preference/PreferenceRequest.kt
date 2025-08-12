package com.stellan.challang.data.model.preference

data class PreferenceRequest(
    val typeIds: List<Int>,
    val levelId: Int,
    val tagIds: List<Int>
)
