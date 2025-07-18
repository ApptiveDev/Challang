package com.challang.backend.review.dto.response;

import com.challang.backend.review.entity.Review;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReviewResponseDto(
        Long reviewId,
        Long writerId,
        String writerNickname,
        String content,
        String imageUrl,
        LocalDateTime createdAt,
        Double rating,
        Integer likeCount,
        Integer dislikeCount,
        Integer reportCount,
        List<String> hashtags
) {
    public static ReviewResponseDto from(Review review, String s3BaseUrl) {
        String fullImageUrl = s3BaseUrl + "/" + review.getImageUrl();

        List<String> tagNames = review.getReviewTags().stream()
                .map(reviewTag -> reviewTag.getTag().getName())
                .collect(Collectors.toList());

        return new ReviewResponseDto(
                review.getId(),
                review.getWriter().getUserId(),
                review.getWriter().getNickname(),
                review.getContent(),
                fullImageUrl,
                review.getCreatedAt(),
                review.getRating(),
                review.getLikeCount() != null ? review.getLikeCount() : 0,
                review.getDislikeCount() != null ? review.getDislikeCount() : 0,
                review.getReportCount() != null ? review.getReportCount() : 0,
                tagNames
        );
    }
}