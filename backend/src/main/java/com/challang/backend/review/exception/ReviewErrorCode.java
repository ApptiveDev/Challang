package com.challang.backend.review.exception;

import com.challang.backend.util.response.ResponseStatus;
import lombok.*;
import org.springframework.http.*;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements ResponseStatus {

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, false, 404, "해당 리뷰를 찾을 수 없습니다."),
    NO_AUTHORITY_FOR_REVIEW(HttpStatus.FORBIDDEN, false, 403, "해당 리뷰에 대한 수정/삭제 권한이 없습니다."),

    INVALID_TAGS_FOR_LIQUOR(HttpStatus.BAD_REQUEST, false, 400, "해당 주류에 속하지 않은 태그가 포함되어 있습니다."),
    ALREADY_REACTED(HttpStatus.CONFLICT, false, 409, "이미 해당 리뷰에 추천 또는 비추천을 하셨습니다."),
    ALREADY_REPORTED(HttpStatus.CONFLICT, false, 409, "이미 신고한 리뷰입니다."),
    MAX_TAGS_EXCEEDED(HttpStatus.BAD_REQUEST, false, 400, "해시태그는 최대 3개까지 선택 가능합니다.");

    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;

}