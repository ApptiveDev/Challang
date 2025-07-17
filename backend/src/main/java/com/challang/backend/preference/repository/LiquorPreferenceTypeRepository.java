package com.challang.backend.preference.repository;


import com.challang.backend.liquor.entity.LiquorType;
import com.challang.backend.user.entity.User;
import com.challang.backend.preference.entity.LiquorPreferenceType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquorPreferenceTypeRepository extends JpaRepository<LiquorPreferenceType, Long> {
    boolean existsByUserAndLiquorType(User user, LiquorType liquorType);
    boolean existsByUser(User user);
    List<LiquorPreferenceType> findByUser(User user);

    @Modifying
    @Query("DELETE FROM LiquorPreferenceType lpt WHERE lpt.user = :user")
    void deleteAllByUser(@Param("user") User user);
}
