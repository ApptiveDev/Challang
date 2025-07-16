package com.stellan.challang.data.model.auth

data class HttpStatusInfo(
    val error: Boolean,
    val is1xxInformational: Boolean,
    val is2xxSuccessful: Boolean,
    val is3xxRedirection: Boolean,
    val is4xxClientError: Boolean,
    val is5xxServerError: Boolean
)
