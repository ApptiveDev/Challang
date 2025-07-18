package com.challang.backend.liquor.repository;

import com.challang.backend.liquor.entity.Liquor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquorRepository extends JpaRepository<Liquor, Long> {

    @Query("""
                SELECT DISTINCT l FROM Liquor l
                LEFT JOIN FETCH l.level
                LEFT JOIN FETCH l.type
                LEFT JOIN FETCH l.liquorTags lt
                LEFT JOIN FETCH lt.tag
                WHERE l.id = :id
            """)
    Optional<Liquor> findWithAllRelationsById(@Param("id") Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
                SELECT DISTINCT l FROM Liquor l
                LEFT JOIN FETCH l.level
                LEFT JOIN FETCH l.type
                LEFT JOIN FETCH l.liquorTags lt
                LEFT JOIN FETCH lt.tag t
                WHERE (:cursor IS NULL OR l.name < :cursor)
                AND (
                    :keyword IS NULL
                    OR LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
                ORDER BY l.name ASC
            """)
    List<Liquor> findAllWithTagsByCursor(
            @Param("cursor") String cursor,
            @Param("keyword") String keyword,
            Pageable pageable
    );


    @Query("""
                SELECT DISTINCT l FROM Liquor l
                LEFT JOIN FETCH l.level
                LEFT JOIN FETCH l.type
                LEFT JOIN FETCH l.liquorTags lt
                LEFT JOIN FETCH lt.tag
                WHERE l.type.id IN :typeIds AND l.level.id IN :levelIds
            """)
    List<Liquor> findWithTagsByTypeAndLevel(@Param("typeIds") Set<Long> typeIds,
                                            @Param("levelIds") Set<Long> levelIds);


}
