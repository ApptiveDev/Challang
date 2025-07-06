package com.challang.backend.review.repository;

import com.challang.backend.review.entity.Review;
import com.challang.backend.review.entity.ReviewReaction;
import com.challang.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, Long> {

    /**
     * 특정 사용자가 특정 리뷰에 대해 남긴 반응을 조회합니다.
     * 사용자가 이미 추천/비추천을 했는지 확인하기 위해 사용됩니다.
     *
     * @param user   사용자 엔티티
     * @param review 리뷰
     * @return Optional<ReviewReaction> 객체 (존재하지 않을 수 있음)
     */
    Optional<ReviewReaction> findByUserAndReview(User user, Review review);

    /**
     * 특정 사용자가 특정 리뷰에 대해 남긴 반응을 삭제합니다.
     * 추천/비추천을 취소(토글)할 때 사용됩니다.
     *
     * @param user   사용자 엔티티
     * @param reviewId 리뷰의 ID
     */
    void deleteByUserAndReviewId(User user, Long reviewId);

    @Modifying
    @Query("delete from ReviewReaction rr where rr.user = :user")
    void deleteAllByUser(@Param("user") User user);

    @Modifying
    @Query("delete from ReviewReaction rr where rr.review = :review")
    void deleteAllByReview(@Param("review") Review review);

}