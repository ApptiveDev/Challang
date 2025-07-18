package com.challang.backend.tag.controller;

import com.challang.backend.tag.code.TagCode;
import com.challang.backend.tag.dto.request.TagRequest;
import com.challang.backend.tag.dto.response.TagResponse;
import com.challang.backend.tag.service.TagService;
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

@Tag(name = "Tag", description = "태그 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "[관리자] 태그 생성", description = "새로운 태그를 등록합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 유효성 검증 실패"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 태그 이름")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<TagResponse>> create(@RequestBody @Valid TagRequest request) {
        TagResponse response = tagService.create(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "태그 단건 조회", description = "ID로 특정 태그를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 태그가 존재하지 않음")
    })
    @GetMapping("/{tagId}")
    public ResponseEntity<BaseResponse<TagResponse>> findById(@PathVariable Long tagId) {
        TagResponse response = tagService.findById(tagId);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "태그 전체 조회", description = "등록된 모든 태그를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 전체 조회 성공")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<TagResponse>>> findAll() {
        List<TagResponse> response = tagService.findAll();
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "[관리자] 태그 수정", description = "특정 태그의 이름을 수정합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 유효성 검증 실패"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 태그가 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "중복된 태그 이름 존재")
    })
    @PutMapping("/{tagId}")
    public ResponseEntity<BaseResponse<TagResponse>> update(
            @PathVariable Long tagId,
            @RequestBody @Valid TagRequest request
    ) {
        TagResponse response = tagService.update(tagId, request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "[관리자] 태그 삭제", description = "특정 태그를 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 태그가 존재하지 않음")
    })
    @DeleteMapping("/{tagId}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long tagId) {
        tagService.delete(tagId);
        return ResponseEntity.ok(new BaseResponse<>(TagCode.TAG_DELETE_SUCCESS));
    }
}
