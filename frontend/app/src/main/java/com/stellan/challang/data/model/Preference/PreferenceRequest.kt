package com.stellan.challang.data.model.Preference

data class PreferenceRequest(
    val typeIds: List<Int>,
    val levelId: Int,
    val tagIds: List<Int>
)
