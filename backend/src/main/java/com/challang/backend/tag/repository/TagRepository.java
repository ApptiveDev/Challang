package com.challang.backend.tag.repository;

import com.challang.backend.tag.entity.Tag;
import com.challang.backend.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByName(String name);

    /**
     * 사용자가 'GOOD' 피드백을 남긴 주류의 태그를 빈도순으로 조회합니다.
     * @param user 사용자
     * @param pageable 페이지 정보 (상위 6개)
     * @return 태그 목록
     */
    @Query("SELECT t " +
            "FROM LiquorFeedback lf " +
            "JOIN lf.liquor l " +
            "JOIN l.liquorTags lt " +
            "JOIN lt.tag t " +
            "WHERE lf.user = :user AND lf.type = com.challang.backend.feedback.entity.FeedbackType.GOOD " +
            "GROUP BY t " +
            "ORDER BY COUNT(t) DESC")
    List<Tag> findTopTagsByUserWithGoodFeedback(@Param("user") User user, Pageable pageable);
}