package com.challang.backend.archive.repository;

import com.challang.backend.archive.entity.Archive;
import com.challang.backend.liquor.entity.Liquor;
import com.challang.backend.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {


    Optional<Archive> findByUserAndLiquor(User user, Liquor liquor);
    boolean existsByUserAndLiquor(User user, Liquor liquor);

    @Modifying
    @Query("DELETE FROM Archive a WHERE a.user = :user AND a.liquor = :liquor")
    int deleteByUserAndLiquor(@Param("user") User user, @Param("liquor") Liquor liquor);

    @Query("""
                SELECT DISTINCT a.liquor
                FROM Archive a
                JOIN a.liquor l
                LEFT JOIN l.liquorTags lt
                LEFT JOIN lt.tag t
                WHERE a.user.userId = :userId
                AND (
                    :keyword IS NULL
                    OR LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
                AND (:cursor IS NULL OR a.id < :cursor)
                ORDER BY a.id DESC
            """)
    List<Liquor> findArchivedLiquorsByCursor(
            @Param("userId") Long userId,
            @Param("keyword") String keyword,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    // 술 id만 얻는 용도 => joinX
    List<Archive> findByUser(User user);

    @Modifying
    @Query("DELETE FROM Archive a WHERE a.user = :user")
    void deleteAllByUser(@Param("user") User user);

}
