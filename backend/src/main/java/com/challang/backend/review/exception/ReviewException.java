package com.challang.backend.review.exception;

import com.challang.backend.global.exception.BaseException;

public class ReviewException extends BaseException {
    public ReviewException(ReviewErrorCode reviewErrorCode) {
        super(reviewErrorCode);
    }
}