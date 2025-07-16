package com.challang.backend.user.controller;

import com.challang.backend.auth.annotation.CurrentUser;
import com.challang.backend.user.dto.UserActivityCountsResponse;
import com.challang.backend.user.dto.UserInfoResponse;
import com.challang.backend.user.entity.User;
import com.challang.backend.user.service.UserService;
import com.challang.backend.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 활동 수 조회", description = "현재 로그인된 사용자가 찜한 주류와 작성한 리뷰의 총 개수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/activity-counts")
    public ResponseEntity<BaseResponse<UserActivityCountsResponse>> getUserActivityCounts(@CurrentUser User user) {
        UserActivityCountsResponse response = userService.getUserActivityCounts(user.getUserId());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 사용자의 닉네임, 성별, 생년월일 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserInfoResponse>> getUserInfo(@CurrentUser User user) {
        UserInfoResponse response = userService.getUserInfo(user.getUserId());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

}