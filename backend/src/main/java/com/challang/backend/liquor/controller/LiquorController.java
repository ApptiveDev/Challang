package com.challang.backend.liquor.controller;

import com.challang.backend.liquor.code.LiquorCode;
import com.challang.backend.liquor.dto.request.*;
import com.challang.backend.liquor.dto.response.*;
import com.challang.backend.liquor.service.LiquorService;
import com.challang.backend.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Liquor", description = "주류 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/liquors")
public class LiquorController {

    private final LiquorService liquorService;

    @Operation(summary = "[관리자] 주류 등록", description = "새로운 주류를 등록합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주류 등록 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 유효성 검증 실패"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 주류 이름")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<LiquorResponse>> create(@RequestBody @Valid LiquorCreateRequest request) {
        LiquorResponse response = liquorService.create(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "주류 단건 조회", description = "ID로 특정 주류를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주류 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 주류가 존재하지 않음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LiquorResponse>> findById(@PathVariable Long id) {
        LiquorResponse response = liquorService.findById(id);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "주류 전체 조회", description = "이름 또는 태그 기준 키워드 검색과 커서 방식으로 주류 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주류 전체 조회 성공")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<LiquorListResponse>> findAll(
            @RequestParam(required = false) String cursorName,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword
    ) {
        LiquorListResponse response = liquorService.findAll(cursorName, pageSize, keyword);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "[관리자] 주류 수정", description = "주류 정보를 수정합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주류 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 유효성 검증 실패"),
            @ApiResponse(responseCode = "404", description = "해당 주류가 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "중복된 주류 이름 존재")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<LiquorResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid LiquorUpdateRequest request
    ) {
        LiquorResponse response = liquorService.update(id, request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }


    @Operation(summary = "[관리자] 주류 삭제", description = "특정 주류를 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주류 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 주류가 존재하지 않음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        liquorService.delete(id);
        return ResponseEntity.ok(new BaseResponse<>(LiquorCode.LIQUOR_DELETE_SUCCESS));
    }

}
