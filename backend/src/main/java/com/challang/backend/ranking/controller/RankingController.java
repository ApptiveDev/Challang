package com.challang.backend.ranking.controller;

import com.challang.backend.ranking.dto.RankingResponseDto;
import com.challang.backend.ranking.service.RankingService;
import com.challang.backend.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ranking", description = "랭킹 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "인기 주류 랭킹 TOP 8 조회", description = "사용자들이 가장 많이 찜한 주류 순으로 8개를 조회합니다.")
    @GetMapping("/top8")
    public ResponseEntity<BaseResponse<List<RankingResponseDto>>> getTop8ArchivedLiquors() {
        List<RankingResponseDto> response = rankingService.getTop8ArchivedLiquors();
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}