package com.challang.backend.recommend.service;

import com.challang.backend.archive.repository.ArchiveRepository;
import com.challang.backend.feedback.repository.LiquorFeedbackRepository;
import com.challang.backend.liquor.dto.response.LiquorResponse;
import com.challang.backend.liquor.entity.Liquor;
import com.challang.backend.liquor.repository.LiquorRepository;
import com.challang.backend.preference.entity.LiquorPreferenceTag;
import com.challang.backend.preference.repository.*;
import com.challang.backend.review.repository.ReviewRepository;
import com.challang.backend.tag.dto.response.TagResponse;
import com.challang.backend.tag.entity.LiquorTag;
import com.challang.backend.tag.entity.Tag;
import com.challang.backend.tag.repository.TagRepository;
import com.challang.backend.user.entity.User;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final LiquorRepository liquorRepository;
    private final LiquorPreferenceTypeRepository preferenceTypeRepository;
    private final LiquorPreferenceLevelRepository preferenceLevelRepository;
    private final LiquorPreferenceTagRepository preferenceTagRepository;

    private final LiquorFeedbackRepository feedbackRepository;
    private final ArchiveRepository archiveRepository;
    private final ReviewRepository reviewRepository;
    private final TagRepository tagRepository;


    @Value("${cloud.aws.s3.url}")
    private String s3BaseUrl;

    public List<LiquorResponse> recommendLiquor(User user) {
        // 1. 선호 정보 가져오기
        // 주종
        Set<Long> typeIds = preferenceTypeRepository.findByUser(user).stream()
                .map((p) -> p.getLiquorType().getId()).collect(Collectors.toSet());

        // 도수
        Set<Long> levelIds = preferenceLevelRepository.findByUser(user).stream()
                .map((p) -> p.getLiquorLevel().getId()).collect(Collectors.toSet());

        // 태그
        Set<Tag> preferredTags = preferenceTagRepository.findByUser(user).stream()
                .map(LiquorPreferenceTag::getTag).collect(Collectors.toSet());

        // 2. 필터링 (주종 + 도수)
        List<Liquor> liquors = liquorRepository.findWithTagsByTypeAndLevel(typeIds, levelIds);

        // 3. 피드백, 아카이브, 리뷰 제외
        Set<Long> excludeIds = new HashSet<>();
        feedbackRepository.findByUser(user).forEach(f -> {
            excludeIds.add(f.getLiquor().getId());
        });
        archiveRepository.findByUser(user).forEach(a -> {
            excludeIds.add(a.getLiquor().getId());
        });
        reviewRepository.findByWriter(user).forEach(r -> {
            excludeIds.add(r.getLiquor().getId());
        });

        liquors = liquors.stream()
                .filter(l -> !excludeIds.contains(l.getId()))
                .toList();

        // 4. 유사도 계산
        List<LiquorScore> scores = liquors.stream()
                .map(l -> new LiquorScore(l, calculateJaccard(preferredTags, l)))
                .sorted(Comparator.comparingDouble(LiquorScore::similarity).reversed())
                .limit(10)
                .toList();

        return scores.stream()
                .map(s -> LiquorResponse.fromEntity(s.liquor, s3BaseUrl))
                .toList();

    }

    public List<TagResponse> getRecommendedKeywords(User user) {
        List<Tag> topTags = tagRepository.findTopTagsByUserWithGoodFeedback(user, PageRequest.of(0, 6));

        return topTags.stream()
                .map(TagResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private double calculateJaccard(Set<Tag> userTags, Liquor liquor) {
        Set<Tag> liquorTags = liquor.getLiquorTags().stream()
                .map(LiquorTag::getTag).collect(Collectors.toSet());

        if (userTags.isEmpty() || liquorTags.isEmpty()) {
            return 0.0;
        }

        Set<Tag> intersection = new HashSet<>(userTags);
        intersection.retainAll(userTags);

        Set<Tag> union = new HashSet<>(userTags);
        union.addAll(liquorTags);

        return (double) intersection.size() / union.size();
    }


    private record LiquorScore(Liquor liquor, double similarity) {
    }

}