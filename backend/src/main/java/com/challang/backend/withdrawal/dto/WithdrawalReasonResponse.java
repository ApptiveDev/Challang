package com.challang.backend.withdrawal.dto;

import com.challang.backend.withdrawal.entity.ReasonType;
import com.challang.backend.withdrawal.entity.WithdrawalReason;

import java.time.LocalDateTime;

public record WithdrawalReasonResponse(
        Long id,
        Long userId,
        ReasonType reason,
        LocalDateTime createdAt
) {
    public static WithdrawalReasonResponse from(WithdrawalReason withdrawalReason) {
        return new WithdrawalReasonResponse(
                withdrawalReason.getId(),
                withdrawalReason.getUserId(),
                withdrawalReason.getReason(),
                withdrawalReason.getCreatedAt()
        );
    }
}