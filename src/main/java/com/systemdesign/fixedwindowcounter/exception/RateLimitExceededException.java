package com.systemdesign.fixedwindowcounter.exception;

import com.systemdesign.fixedwindowcounter.common.exception.BusinessException;
import com.systemdesign.fixedwindowcounter.common.exception.ExceptionCode;

public class RateLimitExceededException extends BusinessException {

    public RateLimitExceededException(ExceptionCode exceptionCode, Object... rejectedValues) {
        super(exceptionCode, rejectedValues);
    }
}
