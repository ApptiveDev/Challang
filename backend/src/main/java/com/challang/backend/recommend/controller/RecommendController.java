package com.challang.backend.recommend.controller;

import com.challang.backend.auth.annotation.CurrentUser;
import com.challang.backend.liquor.dto.response.LiquorResponse;
import com.challang.backend.recommend.service.RecommendService;
import com.challang.backend.tag.dto.response.TagResponse;
import com.challang.backend.user.entity.User;
import com.challang.backend.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Recommend", description = "추천 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    @Operation(summary = "술 추천", description = "선호도와 피드백 기반 추천 술 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 조회 성공")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<LiquorResponse>>> getRecommendList(
            @CurrentUser User user
    ) {
        List<LiquorResponse> response = recommendService.recommendLiquor(user);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "추천 키워드 조회", description = "사용자가 '좋아요'한 주류의 키워드를 기반으로 추천 키워드 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 키워드 조회 성공")
    })
    @GetMapping("/keywords")
    public ResponseEntity<BaseResponse<List<TagResponse>>> getRecommendedKeywords(
            @CurrentUser User user
    ) {
        List<TagResponse> response = recommendService.getRecommendedKeywords(user);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}