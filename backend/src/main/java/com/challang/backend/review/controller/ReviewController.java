package com.challang.backend.review.controller;

import com.challang.backend.auth.annotation.CurrentUser;
import com.challang.backend.review.dto.request.ReviewCreateRequestDto;
import com.challang.backend.review.dto.request.ReviewUpdateRequestDto;
import com.challang.backend.review.dto.response.ReviewResponseDto;
import com.challang.backend.review.service.ReviewService;
import com.challang.backend.user.entity.User;
import com.challang.backend.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import com.challang.backend.review.dto.request.ReportRequestDto;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Tag(name = "Review", description = "리뷰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 생성", description = "특정 주류에 대한 리뷰를 작성합니다. (인증 필요)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 주류 정보를 찾을 수 없음")
    })
    @PostMapping("/liquors/{liquorId}/reviews")
    public ResponseEntity<BaseResponse<ReviewResponseDto>> createReview(
            @PathVariable Long liquorId,
            @RequestBody @Valid ReviewCreateRequestDto request,
            @CurrentUser User user) {
        ReviewResponseDto responseDto = reviewService.createReview(liquorId, request, user.getUserId());
        return ResponseEntity.ok(new BaseResponse<>(responseDto));
    }

    @Operation(summary = "특정 주류의 리뷰 목록 조회", description = "태그로 필터링하거나, 최신순/추천순으로 정렬하여 리뷰 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "주류 정보를 찾을 수 없음")
    })
    @GetMapping("/liquors/{liquorId}/reviews")
    public ResponseEntity<BaseResponse<Page<ReviewResponseDto>>> getReviews(
            @Parameter(description = "주류 ID") @PathVariable Long liquorId,
            @Parameter(description = "필터링할 태그 ID 목록") @RequestParam(required = false) List<Long> tagIds,
            @Parameter(description = "정렬 조건 (예: 'createdAt,desc' 또는 'likeCount,desc')")
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ReviewResponseDto> responsePage = reviewService.getReviewsByLiquor(liquorId, tagIds, pageable);
        return ResponseEntity.ok(new BaseResponse<>(responsePage));
    }

    @Operation(summary = "리뷰 수정", description = "자신이 작성한 리뷰를 수정합니다. (인증 필요)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
    })
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<BaseResponse<ReviewResponseDto>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto request,
            @CurrentUser User user) {
        ReviewResponseDto responseDto = reviewService.updateReview(reviewId, request, user);
        return ResponseEntity.ok(new BaseResponse<>(responseDto));
    }

    @Operation(summary = "리뷰 삭제", description = "자신이 작성한 리뷰를 삭제합니다. (인증 필요)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
    })
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<BaseResponse<String>> deleteReview(
            @PathVariable Long reviewId,
            @CurrentUser User user) {
        reviewService.deleteReview(reviewId, user);
        return ResponseEntity.ok(new BaseResponse<>("리뷰가 성공적으로 삭제되었습니다."));
    }

    @Operation(summary = "리뷰 추천/추천 취소", description = "리뷰를 추천하거나 추천을 취소합니다. (인증 필요)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "리뷰 또는 사용자 정보를 찾을 수 없음")
    })
    @PostMapping("/reviews/{reviewId}/like")
    public ResponseEntity<BaseResponse<String>> likeReview(
            @PathVariable Long reviewId,
            @CurrentUser User user) {
        reviewService.likeReview(reviewId, user);
        return ResponseEntity.ok(new BaseResponse<>("요청이 처리되었습니다."));
    }

    @Operation(summary = "리뷰 비추천/비추천 취소", description = "리뷰를 비추천하거나 비추천을 취소합니다. (인증 필요)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "리뷰 또는 사용자 정보를 찾을 수 없음")
    })
    @PostMapping("/reviews/{reviewId}/dislike")
    public ResponseEntity<BaseResponse<String>> dislikeReview(
            @PathVariable Long reviewId,
            @CurrentUser User user) {
        reviewService.dislikeReview(reviewId, user);
        return ResponseEntity.ok(new BaseResponse<>("요청이 처리되었습니다."));
    }

    @Operation(summary = "리뷰 신고", description = "리뷰를 신고합니다. (인증 필요)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고 접수 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "리뷰 또는 사용자 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "이미 신고한 리뷰")
    })
    @PostMapping("/reviews/{reviewId}/report")
    public ResponseEntity<BaseResponse<String>> reportReview(
            @PathVariable Long reviewId,
//            @Valid @RequestBody ReportRequestDto request,
            @CurrentUser User user) {

        reviewService.reportReview(reviewId, user);
        return ResponseEntity.ok(new BaseResponse<>("신고가 접수되었습니다."));
    }
}
