package com.challang.backend.liquor.controller;

import com.challang.backend.liquor.code.LiquorCode;
import com.challang.backend.liquor.dto.request.LiquorLevelRequest;
import com.challang.backend.liquor.dto.response.LiquorLevelResponse;
import com.challang.backend.liquor.service.LiquorLevelService;
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

@Tag(name = "Liquor Level", description = "도수 등급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/liquor-levels")
public class LiquorLevelController {

    private final LiquorLevelService liquorLevelService;

    @Operation(summary = "[관리자] 도수 등급 생성", description = "새로운 도수 등급을 등록합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "도수 등급 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 유효성 검증 실패"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 도수 등급 이름")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<LiquorLevelResponse>> create(@RequestBody @Valid LiquorLevelRequest request) {
        LiquorLevelResponse response = liquorLevelService.create(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "도수 등급 단건 조회", description = "ID로 특정 도수 등급을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "도수 등급 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 도수 등급이 존재하지 않음")
    })
    @GetMapping("/{levelId}")
    public ResponseEntity<BaseResponse<LiquorLevelResponse>> findById(@PathVariable Long levelId) {
        LiquorLevelResponse response = liquorLevelService.findById(levelId);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "도수 등급 전체 조회", description = "등록된 모든 도수 등급을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "도수 등급 전체 조회 성공")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<LiquorLevelResponse>>> findAll() {
        List<LiquorLevelResponse> response = liquorLevelService.findAll();
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "[관리자] 도수 등급 수정", description = "특정 도수 등급의 이름을 수정합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "도수 등급 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 유효성 검증 실패"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 도수 등급이 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "중복된 도수 등급 이름 존재")
    })
    @PutMapping("/{levelId}")
    public ResponseEntity<BaseResponse<LiquorLevelResponse>> update(
            @PathVariable Long levelId,
            @RequestBody @Valid LiquorLevelRequest request
    ) {
        LiquorLevelResponse response = liquorLevelService.update(levelId, request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "[관리자] 도수 등급 삭제", description = "특정 도수 등급을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "도수 등급 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 도수 등급이 존재하지 않음")
    })
    @DeleteMapping("/{levelId}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long levelId) {
        liquorLevelService.delete(levelId);
        return ResponseEntity.ok(new BaseResponse<>(LiquorCode.LIQUOR_LEVEL_DELETE_SUCCESS));
    }
}
