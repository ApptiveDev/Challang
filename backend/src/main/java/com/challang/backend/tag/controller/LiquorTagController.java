package com.challang.backend.tag.controller;

import com.challang.backend.tag.code.TagCode;
import com.challang.backend.tag.dto.request.LiquorTagRequest;
import com.challang.backend.tag.dto.response.LiquorTagResponse;
import com.challang.backend.tag.service.LiquorTagService;
import com.challang.backend.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LiquorTag", description = "주류-태그 연결 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/liquors/{liquorId}/tags")
public class LiquorTagController {

    private final LiquorTagService liquorTagService;

    @Operation(summary = "[관리자] 주류에 태그 추가", description = "특정 주류에 태그를 추가합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주류 태그 추가 성공"),
            @ApiResponse(responseCode = "404", description = "해당 주류 또는 태그가 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<LiquorTagResponse>> create(
            @PathVariable Long liquorId,
            @RequestBody @Valid LiquorTagRequest request
    ) {
        LiquorTagResponse response = liquorTagService.create(liquorId, request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "특정 주류의 태그 조회", description = "해당 주류에 연결된 태그 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 주류가 존재하지 않음")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<LiquorTagResponse>>> findByLiquorId(@PathVariable Long liquorId) {
        List<LiquorTagResponse> response = liquorTagService.findByLiquorId(liquorId);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "[관리자] 주류 태그 삭제", description = "주류에 연결된 특정 태그를 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 주류 또는 태그가 존재하지 않음")
    })
    @DeleteMapping("/{liquorTagId}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long liquorId, @PathVariable Long liquorTagId) {
        liquorTagService.delete(liquorId, liquorTagId);
        return ResponseEntity.ok(new BaseResponse<>(TagCode.LIQUOR_TAG_DELETE_SUCCESS));
    }
}
