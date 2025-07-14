package com.challang.backend.user.dto;

import com.challang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "사용자 정보 응답 DTO")
public record UserInfoResponse(
        @Schema(description = "사용자 닉네임", example = "챌린저")
        String nickname,

        @Schema(description = "사용자 성별 (0: 남자, 1: 여자)", example = "0")
        Integer gender,

        @Schema(description = "사용자 생년월일", example = "2000-01-01")
        LocalDate birthDate
) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getNickname(),
                user.getGender(),
                user.getBirthDate()
        );
    }
}