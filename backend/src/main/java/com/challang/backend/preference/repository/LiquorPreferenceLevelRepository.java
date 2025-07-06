package com.challang.backend.preference.repository;


import com.challang.backend.preference.entity.LiquorPreferenceLevel;
import com.challang.backend.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquorPreferenceLevelRepository extends JpaRepository<LiquorPreferenceLevel, Long> {
    List<LiquorPreferenceLevel> findByUser(User user);

    @Modifying
    @Query("DELETE FROM LiquorPreferenceLevel lpl WHERE lpl.user = :user")
    void deleteAllByUser(@Param("user") User user);
}
