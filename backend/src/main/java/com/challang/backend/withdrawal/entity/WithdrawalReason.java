package com.challang.backend.withdrawal.entity;

import com.challang.backend.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "withdrawal_reason")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private ReasonType reason;

    @Builder
    public WithdrawalReason(Long userId, ReasonType reason) {
        this.userId = userId;
        this.reason = reason;
    }
}