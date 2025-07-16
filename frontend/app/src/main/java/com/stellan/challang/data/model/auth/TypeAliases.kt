package com.stellan.challang.data.model.auth

typealias KakaoSignInResponse = ApiResponseStringStatus<TokenPairResponse>
typealias KakaoSignupResponse = ApiResponseStringStatus<Boolean>
typealias LogoutResponse = ApiResponseStringStatus<String>
typealias WithdrawResponse = ApiResponseStringStatus<String>

