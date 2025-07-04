package com.challang.backend.preference.repository;

import com.challang.backend.tag.entity.Tag;
import com.challang.backend.user.entity.User;
import com.challang.backend.preference.entity.LiquorPreferenceTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquorPreferenceTagRepository extends JpaRepository<LiquorPreferenceTag, Long> {
    boolean existsByUserAndTag(User user, Tag tag);

    List<LiquorPreferenceTag> findByUser(User user);
}
