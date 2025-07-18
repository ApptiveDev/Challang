package com.challang.backend.review.dto.request;

import java.util.List;

public record ReviewUpdateRequestDto(
        String content,
        String imageUrl,
        Double rating,
        List<Long> tagIds
) {
}