package com.challang.backend.user.dto;

public record UserActivityCountsResponse(
        long likedCurationCount,
        long writtenReviewCount
) {
}