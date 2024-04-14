package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import org.springframework.http.HttpStatus;

public class CommonException extends BaseException {

    public CommonException(BaseResponse<?> baseResponse) {
        super(baseResponse.getCode(), baseResponse.getMessage());
    }

    public CommonException(String message) {
        super(HttpStatus.BAD_GATEWAY.value(), message);
    }

}
