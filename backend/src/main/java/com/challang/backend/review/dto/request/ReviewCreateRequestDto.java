package com.challang.backend.review.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record ReviewCreateRequestDto(
        @NotBlank(message = "리뷰 내용은 필수 입력값입니다.")
        String content,

        @NotBlank(message = "리뷰 이미지는 필수입니다.")
        String imageUrl,

        @NotNull @Min(1) @Max(5) int rating,

        @NotEmpty @Size(max = 3, message = "해시태그는 최대 3개까지 선택 가능합니다.") List<Long> tagIds
) {
}