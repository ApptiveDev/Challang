package com.challang.backend.review.repository;

import com.challang.backend.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReviewRepositoryCustom {
    Page<Review> findReviewsByLiquorAndTags(Long liquorId, List<Long> tagIds, Pageable pageable);
}