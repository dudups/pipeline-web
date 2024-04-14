package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class StageEmptyException extends BaseException {

    public StageEmptyException() {
        super(HttpStatus.BAD_REQUEST.value(), "阶段不能为空");
    }

}
