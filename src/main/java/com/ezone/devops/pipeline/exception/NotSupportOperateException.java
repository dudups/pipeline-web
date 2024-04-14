package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class NotSupportOperateException extends BaseException {

    public NotSupportOperateException() {
        super(HttpStatus.BAD_REQUEST.value(), "不支持的操作");
    }

}
