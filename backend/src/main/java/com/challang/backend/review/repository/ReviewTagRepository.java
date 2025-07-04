package com.challang.backend.review.repository;

import com.challang.backend.liquor.dto.response.TagStatDto;
import com.challang.backend.review.entity.Review;
import com.challang.backend.review.entity.ReviewTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {

    @Query("SELECT new com.challang.backend.liquor.dto.response.TagStatDto(" +
            "    rt.tag.name AS tagName, " +
            "    (CAST(COUNT(DISTINCT rt.review.id) AS double) * 100.0 / l.reviewCount)" +
            ") " +
            "FROM ReviewTag rt JOIN rt.review.liquor l " +
            "WHERE l.id = :liquorId " +
            "GROUP BY rt.tag.name, l.reviewCount " +
            "ORDER BY COUNT(DISTINCT rt.review.id) DESC, rt.tag.name ASC")
    List<TagStatDto> findTopTagStatsByLiquorId(@Param("liquorId") Long liquorId, Pageable pageable);
    @Modifying
    @Query("delete from ReviewTag rt where rt.review = :review")
    void deleteByReview(@Param("review") Review review);

}