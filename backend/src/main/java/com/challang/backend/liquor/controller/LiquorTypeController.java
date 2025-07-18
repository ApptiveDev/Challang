package com.challang.backend.liquor.controller;

import com.challang.backend.liquor.code.LiquorCode;
import com.challang.backend.liquor.dto.request.LiquorTypeRequest;
import com.challang.backend.liquor.dto.response.LiquorTypeResponse;
import com.challang.backend.liquor.service.LiquorTypeService;
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

@Tag(name = "Liquor Type", description = "주종 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/liquor-types")
public class LiquorTypeController {

    private final LiquorTypeService liquorTypeService;

    @Operation(summary = "[관리자] 주종 생성", description = "새로운 주종을 등록합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주종 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 유효성 검증 실패"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 주종 이름")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<LiquorTypeResponse>> create(@RequestBody @Valid LiquorTypeRequest request) {
        LiquorTypeResponse response = liquorTypeService.create(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "주종 단건 조회", description = "ID로 특정 주종을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주종 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 주종이 존재하지 않음")
    })
    @GetMapping("/{typeId}")
    public ResponseEntity<BaseResponse<LiquorTypeResponse>> findById(@PathVariable Long typeId) {
        LiquorTypeResponse response = liquorTypeService.findById(typeId);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "주종 전체 조회", description = "등록된 모든 주종을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주종 전체 조회 성공")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<LiquorTypeResponse>>> findAll() {
        List<LiquorTypeResponse> response = liquorTypeService.findAll();
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "[관리자] 주종 수정", description = "특정 주종의 이름을 수정합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주종 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 유효성 검증 실패"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 주종이 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "중복된 주종 이름 존재")
    })
    @PutMapping("/{typeId}")
    public ResponseEntity<BaseResponse<LiquorTypeResponse>> update(
            @PathVariable Long typeId,
            @RequestBody @Valid LiquorTypeRequest request
    ) {
        LiquorTypeResponse response = liquorTypeService.update(typeId, request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "[관리자] 주종 삭제", description = "특정 주종을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주종 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 주종이 존재하지 않음")
    })
    @DeleteMapping("/{typeId}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long typeId) {
        liquorTypeService.delete(typeId);
        return ResponseEntity.ok(new BaseResponse<>(LiquorCode.LIQUOR_TYPE_DELETE_SUCCESS));
    }
}

