package com.challang.backend.review.service;

import com.challang.backend.global.exception.BaseException;
import com.challang.backend.liquor.code.LiquorCode;
import com.challang.backend.liquor.entity.Liquor;
import com.challang.backend.liquor.repository.LiquorRepository;
import com.challang.backend.liquor.service.LiquorService;
import com.challang.backend.review.dto.request.*;
import com.challang.backend.review.dto.response.*;
import com.challang.backend.review.entity.*;
import com.challang.backend.review.exception.ReviewErrorCode;
import com.challang.backend.review.repository.ReviewReactionRepository;
import com.challang.backend.review.repository.ReviewReportRepository;
import com.challang.backend.review.repository.ReviewRepository;
import com.challang.backend.review.repository.ReviewTagRepository;
import com.challang.backend.tag.entity.Tag;
import com.challang.backend.tag.repository.TagRepository;
import com.challang.backend.user.entity.User;
import com.challang.backend.user.exception.UserErrorCode;
import com.challang.backend.user.repository.UserRepository;
import com.challang.backend.util.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final LiquorRepository liquorRepository;
    private final TagRepository tagRepository;
    private final ReviewTagRepository reviewTagRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private final ReviewReportRepository reviewReportRepository;
    private final S3Service s3Service;
    private final LiquorService liquorService;

    @Value("${cloud.aws.s3.url}")
    private String s3BaseUrl;

    /**
     * 리뷰 생성
     */
    @Transactional
    public ReviewResponseDto createReview(Long liquorId, ReviewCreateRequestDto request, Long userId) {
        // 1. 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다."));

        // 2. Liquor 정보 조회
        Liquor liquor = liquorRepository.findById(liquorId)
                .orElseThrow(() -> new BaseException(LiquorCode.LIQUOR_NOT_FOUND));

        validateTagCount(request.tagIds());


        // 3. Review 엔티티 생성
        Review review = Review.builder()
                .writer(user)
                .liquor(liquor)
                .imageUrl(request.imageUrl())
                .content(request.content())
                .rating((double) request.rating())
                .build();

        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            List<ReviewTag> reviewTags = createReviewTags(review, liquor, request.tagIds());
            review.addTags(reviewTags);
        }

        // 4. DB에 저장
        Review savedReview = reviewRepository.save(review);

        liquorService.updateLiquorStats(liquorId);

        // 5. DTO로 변환하여 반환
        return ReviewResponseDto.from(savedReview, s3BaseUrl);
    }

    /**
     * 특정 주류의 리뷰 목록 조회
     */
    public Page<ReviewResponseDto> getReviewsByLiquor(Long liquorId, List<Long> tagIds, Pageable pageable) {
        if (!liquorRepository.existsById(liquorId)) {
            throw new BaseException(LiquorCode.LIQUOR_NOT_FOUND);
        }
        Page<Review> reviewPage = reviewRepository.findReviewsByLiquorAndTags(liquorId, tagIds, pageable);
        return reviewPage.map(review -> ReviewResponseDto.from(review, s3BaseUrl));
    }


    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto request, User user) {
        Review review = findReview(reviewId, user.getUserId());

        validateTagCount(request.tagIds());

        if (request.imageUrl() != null && !Objects.equals(review.getImageUrl(), request.imageUrl())) {
            s3Service.deleteByKey(review.getImageUrl());
        }

        reviewTagRepository.deleteByReview(review); // 기존 태그 연결 모두 삭제
        List<ReviewTag> newTags = createReviewTags(review, review.getLiquor(), request.tagIds());
        review.update(request, newTags); // update 메서드 시그니처 변경 필요

        // ⭐ 주류 통계 업데이트
        liquorService.updateLiquorStats(review.getLiquor().getId());

        return ReviewResponseDto.from(review, s3BaseUrl);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = findReview(reviewId, user.getUserId());
        Long liquorId = review.getLiquor().getId(); // 삭제 전 liquorId 확보

        reviewReportRepository.deleteAllByReview(review);
        reviewReactionRepository.deleteAllByReview(review);

        s3Service.deleteByKey(review.getImageUrl());
        reviewRepository.delete(review);

        // ⭐ 주류 통계 업데이트
        liquorService.updateLiquorStats(liquorId);
    }

    @Transactional
    public void likeReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ReviewErrorCode.REVIEW_NOT_FOUND));
        toggleReaction(review, user, ReactionType.LIKE);
    }

    @Transactional
    public void dislikeReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ReviewErrorCode.REVIEW_NOT_FOUND));
        toggleReaction(review, user, ReactionType.DISLIKE);
    }

    @Transactional
    public void reportReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 1. 중복 신고 확인
        if (reviewReportRepository.existsByUserAndReview(user, review)) {
            throw new BaseException(ReviewErrorCode.ALREADY_REPORTED);
        }

        // 2. 신고 기록 저장
        ReviewReport report = ReviewReport.builder()
                .user(user)
                .review(review)
//                .reason(request.reason())
                .build();
        reviewReportRepository.save(report);

        // 3. 리뷰의 신고 카운트 증가
        review.increaseReportCount();
    }

    private Review findReview(Long reviewId, Long userId) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 수정/삭제 권한 확인
        if (!Objects.equals(review.getWriter().getUserId(), userId)) {
            throw new BaseException(ReviewErrorCode.NO_AUTHORITY_FOR_REVIEW);
        }

        return review;
    }

    private List<ReviewTag> createReviewTags(Review review, Liquor liquor, List<Long> tagIds) {
        Set<Long> validTagIds = liquor.getLiquorTags().stream()
                .map(liquorTag -> liquorTag.getTag().getId())
                .collect(Collectors.toSet());

        for (Long tagId : tagIds) {
            if (!validTagIds.contains(tagId)) {
                throw new BaseException(ReviewErrorCode.INVALID_TAGS_FOR_LIQUOR);
            }
        }

        List<Tag> tags = tagRepository.findAllById(tagIds);
        return tags.stream()
                .map(tag -> ReviewTag.builder().review(review).tag(tag).build())
                .collect(Collectors.toList());
    }

    private void toggleReaction(Review review, User user, ReactionType reactionType) {
        reviewReactionRepository.findByUserAndReview(user, review)
                .ifPresentOrElse(
                        reaction -> { // 이미 반응이 있을 때
                            if (reaction.getType() == reactionType) { // 같은 반응이면 취소
                                reviewReactionRepository.delete(reaction);
                                review.decreaseReactionCount(reactionType);
                            } else { // 다른 반응이면 변경
                                review.changeReaction(reaction.getType(), reactionType);
                                reaction.updateType(reactionType);
                            }
                        },
                        () -> { // 반응이 없을 때
                            ReviewReaction newReaction = ReviewReaction.builder()
                                    .review(review).user(user).type(reactionType).build();
                            reviewReactionRepository.save(newReaction);
                            review.increaseReactionCount(reactionType);
                        }
                );
    }

    private void validateTagCount(List<Long> tagIds) {
        // 태그 목록이 비었거나 리스트의 크기가 3보다 크면 예외 발생
        if (tagIds != null && tagIds.size() > 3) {
            throw new BaseException(ReviewErrorCode.MAX_TAGS_EXCEEDED);
        }
    }
}
