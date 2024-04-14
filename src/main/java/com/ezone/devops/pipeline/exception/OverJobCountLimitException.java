package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class OverJobCountLimitException extends BaseException {

    public OverJobCountLimitException() {
        super(HttpStatus.BAD_REQUEST.value(), "发布类型插件只能存在一次");
    }

}