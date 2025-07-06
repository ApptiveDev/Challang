package com.challang.backend.auth.exception;

import com.challang.backend.util.response.ResponseStatus;
import lombok.*;
import org.springframework.http.*;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ResponseStatus {

    // 카카오 관련
    INVALID_KAKAO_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, false, 401, "잘못된 카카오 액세스 토큰입니다."),
    KAKAO_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 500, "카카오 서버 내부 오류입니다."),

    // 토큰 관련
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, false, 404, "리프레시 토큰을 찾을 수 없습니다."),

    // 이메일 관련
    MAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, 500, "인증 이메일 전송 실패"),
    EMAIL_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, false, 400, "이메일 인증에 실패했습니다."),

    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, false, 401, "인증이 필요합니다. 토큰이 유효하지 않습니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, false, 403, "해당 리소스에 접근할 권한이 없습니다."),
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST, false, 400, "로그아웃에 실패했습니다.");

    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}