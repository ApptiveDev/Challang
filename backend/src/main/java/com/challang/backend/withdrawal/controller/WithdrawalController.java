package com.challang.backend.withdrawal.controller;

import com.challang.backend.util.response.BaseResponse;
import com.challang.backend.withdrawal.dto.WithdrawalReasonResponse;
import com.challang.backend.withdrawal.service.WithdrawalReasonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin API", description = "관리자 전용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class WithdrawalController {

    private final WithdrawalReasonService withdrawalReasonService;

    @Operation(summary = "[관리자] 탈퇴 사유 목록 조회", description = "모든 사용자의 탈퇴 사유를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/withdrawal-reasons")
    public ResponseEntity<BaseResponse<List<WithdrawalReasonResponse>>> getWithdrawalReasons() {
        List<WithdrawalReasonResponse> response = withdrawalReasonService.getAllWithdrawalReasons();
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}