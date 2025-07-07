package com.challang.backend.withdrawal.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 탈퇴 사유 (SERVICE_DISSATISFACTION: 서비스 불만족, APP_ERROR: 앱 오류, LACK_OF_INFORMATION: 정보의 부족, REJOIN_WITH_OTHER_ACCOUNT: 다른 계정으로 재가입, OTHER: 기타)")
public enum ReasonType {
    SERVICE_DISSATISFACTION, // 서비스 불만족
    APP_ERROR,               // 앱 오류
    LACK_OF_INFORMATION,     // 정보의 부족
    REJOIN_WITH_OTHER_ACCOUNT, // 다른 계정으로 재가입
    OTHER                    // 기타
}