package com.challang.backend.liquor.service;

import static com.challang.backend.tag.constant.TagConstant.*;

import com.challang.backend.global.exception.BaseException;
import com.challang.backend.liquor.code.LiquorCode;
import com.challang.backend.liquor.dto.request.*;
import com.challang.backend.liquor.dto.response.*;
import com.challang.backend.liquor.entity.*;
import com.challang.backend.liquor.repository.*;
import com.challang.backend.review.repository.ReviewRepository;
import com.challang.backend.tag.code.TagCode;
import com.challang.backend.tag.dto.request.LiquorTagRequest;
import com.challang.backend.tag.entity.*;
import com.challang.backend.tag.repository.*;
import com.challang.backend.util.aws.service.S3Service;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.challang.backend.liquor.dto.response.TagStatDto; // TagStatDto import
import com.challang.backend.review.repository.ReviewTagRepository; // ReviewTagRepository 주입

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Async; // 비동기 처리를 위해 추가


@Service
@Transactional
@RequiredArgsConstructor
public class LiquorService {

    private final LiquorRepository liquorRepository;
    private final LiquorLevelRepository levelRepository;
    private final LiquorTypeRepository typeRepository;
    private final TagRepository tagRepository;
    private final LiquorTagRepository liquorTagRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewTagRepository reviewTagRepository;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.url}")
    private String s3BaseUrl;

    // 주류 추가
    public LiquorResponse create(LiquorCreateRequest request) {
        validateDuplicateName(request.name(), null);
        validateAbvRange(request.minAbv(), request.maxAbv());

        // level, type 존재 여부 확인
        LiquorLevel level = findLevelOrNull(request.levelId());
        LiquorType type = findTypeOrNull(request.typeId());

        Liquor liquor = Liquor.builder()
                .name(request.name())
                .tastingNote(request.tastingNote())
                .origin(request.origin())
                .minAbv(request.minAbv())
                .maxAbv(request.maxAbv())
                .level(level)
                .type(type)
                .imageUrl(request.imageUrl())
                .build();
        Liquor saved = liquorRepository.save(liquor);

        // 태그 설정 (핵심 태그는 2~4개)
        validateCoreTagCount(request.liquorTags());
        List<LiquorTag> tags = convertToLiquorTags(saved, request.liquorTags());
        // saved.getLiquorTags().addAll(tags); // 같은 트랜잭션 내 사용 시 필수
        liquorTagRepository.saveAll(tags);

        return LiquorResponse.fromEntity(saved, s3BaseUrl, Collections.emptyList());
    }

    // 주류 단건 조회
    @Transactional(readOnly = true)
    public LiquorResponse findById(Long id) {
        Liquor liquor = getLiquorById(id);

        List<TagStatDto> topTagStats = reviewTagRepository.findTopTagStatsByLiquorId(id, PageRequest.of(0, 3));

        // fromEntity 메서드 시그니처 변경 필요
        return LiquorResponse.fromEntity(liquor, s3BaseUrl, topTagStats);
    }

    // 주류 전체 조회
    @Transactional(readOnly = true)
    public LiquorListResponse findAll(String cursorName, Integer pageSize,  String keyword) {
        Pageable pageable = PageRequest.of(0, pageSize + 1); // +1: 다음 페이지 여부 확인용

        List<Liquor> liquors = liquorRepository.findAllWithTagsByCursor(cursorName, keyword, pageable);

        boolean hasNext = liquors.size() > pageSize;
        if (hasNext) {
            liquors = liquors.subList(0, pageSize);
        }

        String nextCursor = hasNext ? liquors.get(pageSize - 1).getName() : null;

        return new LiquorListResponse(
                liquors.stream()
                        .map(liquor -> {
                            List<TagStatDto> stats = Collections.emptyList();
                            return LiquorResponse.fromEntity(liquor, s3BaseUrl, stats);
                        }).toList(),
                nextCursor,
                hasNext
        );
    }

    @Async // @EnableAsync 설정 필요
    @Transactional
    public void updateLiquorStats(Long liquorId) {
        Liquor liquor = liquorRepository.findById(liquorId)
                .orElseThrow(() -> new BaseException(LiquorCode.LIQUOR_NOT_FOUND));

        // ⭐ 평균 평점 및 리뷰 수 계산
        Double newAverageRating = reviewRepository.calculateAverageRatingByLiquorId(liquorId).orElse(0.0);
        int newReviewCount = reviewRepository.countByLiquorId(liquorId);

        // ⭐ Liquor 엔티티에 업데이트
        liquor.updateAverageRating(newAverageRating, newReviewCount);
    }

    // 주류 수정
    public LiquorResponse update(Long id, LiquorUpdateRequest request) {
        Liquor liquor = getLiquorById(id);

        if (request.name() != null) {
            validateDuplicateName(request.name(), id);
        }

        if (request.minAbv() != null || request.maxAbv() != null) {
            Double minAbv = request.minAbv() != null ? request.minAbv() : liquor.getMinAbv();
            Double maxAbv = request.maxAbv() != null ? request.maxAbv() : liquor.getMaxAbv();
            validateAbvRange(minAbv, maxAbv);
        }

        LiquorLevel level = findLevelOrNull(request.levelId());
        LiquorType type = findTypeOrNull(request.typeId());
        liquor.update(request, level, type);

        // 태그 업데이트
        if (request.liquorTags() != null) {
            validateCoreTagCount(request.liquorTags());

            // 기존 태그 제거
            liquorTagRepository.deleteByLiquorId(liquor.getId());

            // 새 태그 생성
            List<LiquorTag> updatedTags = convertToLiquorTags(liquor, request.liquorTags());

            // 같은 트랜잭션 내 사용 시 필수
            // liquor.getLiquorTags().clear();
            // liquor.getLiquorTags().addAll(updatedTags);

            liquorTagRepository.saveAll(updatedTags);
        }

        return LiquorResponse.fromEntity(liquor, s3BaseUrl);
    }

    public void delete(Long id) {
        Liquor liquor = getLiquorById(id);

        liquorTagRepository.deleteAll(liquor.getLiquorTags());
        reviewRepository.deleteByLiquorId(liquor.getId());
        s3Service.deleteByKey(liquor.getImageUrl());

        liquorRepository.delete(liquor);
    }


    private List<LiquorTag> convertToLiquorTags(Liquor liquor, List<LiquorTagRequest> liquorTagRequests) {
        List<Long> tagIds = liquorTagRequests.stream().map(LiquorTagRequest::id).toList();
        List<Tag> tags = tagRepository.findAllById(tagIds);

        if (tags.size() != tagIds.size()) {
            throw new BaseException(TagCode.TAG_NOT_FOUND);
        }

        Map<Long, Tag> tagMap = tags.stream().collect(Collectors.toMap(Tag::getId, Function.identity()));

        return liquorTagRequests.stream()
                .map(req -> LiquorTag.builder()
                        .liquor(liquor)
                        .tag(tagMap.get(req.id()))
                        .isCore(req.isCore())
                        .build())
                .toList();
    }

    private void validateCoreTagCount(List<LiquorTagRequest> liquorTagRequests) {
        long tagCount = liquorTagRequests.stream().filter(LiquorTagRequest::isCore).count();

        if (tagCount < MIN_CORE_TAG || tagCount > MAX_CORE_TAG) {
            throw new BaseException(TagCode.INVALID_CORE_TAG_COUNT);
        }
    }


    private void validateDuplicateName(String name, Long excludeId) {
        boolean exits = excludeId == null
                ? liquorRepository.existsByName(name)
                : liquorRepository.existsByNameAndIdNot(name, excludeId);

        if (exits) {
            throw new BaseException(LiquorCode.LIQUOR_ALREADY_EXISTS);
        }
    }


    private void validateAbvRange(Double minAbv, Double maxAbv) {
        if (minAbv > maxAbv) {
            throw new BaseException(LiquorCode.INVALID_ABV_RANGE);
        }

        if (minAbv < 0 || maxAbv > 100) {
            throw new BaseException(LiquorCode.INVALID_ABV_VALUE);
        }
    }


    private Liquor getLiquorById(Long id) {
        return liquorRepository.findWithAllRelationsById(id)
                .orElseThrow(() -> new BaseException(LiquorCode.LIQUOR_NOT_FOUND));
    }

    private LiquorLevel findLevelOrNull(Long levelId) {
        if (levelId == null) {
            return null;
        }
        return levelRepository.findById(levelId)
                .orElseThrow(() -> new BaseException(LiquorCode.LIQUOR_LEVEL_NOT_FOUND));
    }

    private LiquorType findTypeOrNull(Long typeId) {
        if (typeId == null) {
            return null;
        }
        return typeRepository.findById(typeId)
                .orElseThrow(() -> new BaseException(LiquorCode.LIQUOR_TYPE_NOT_FOUND));
    }

}
