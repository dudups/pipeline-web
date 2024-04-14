package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class OperateNotPermittedException extends BaseException {

    public OperateNotPermittedException() {
        super(HttpStatus.BAD_REQUEST.value(), "不允许的操作类型");
    }

}
