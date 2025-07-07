package com.challang.backend.auth.dto.request;

import com.challang.backend.withdrawal.entity.ReasonType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeletionRequest {

    @NotNull(message = "탈퇴 사유는 필수입니다.")
    @Schema(description = "회원 탈퇴 사유", implementation = ReasonType.class)
    private ReasonType reason;
}