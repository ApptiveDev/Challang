package com.challang.backend.auth.dto.response;


public record TokenResponse(
        String accessToken,
        String refreshToken,
        boolean isPreferenceSet) {
    public TokenResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, false); // 기본값 false
    }
}
