package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import org.springframework.http.HttpStatus;

public class InitialJobException extends BaseException {

    public InitialJobException() {
        super(HttpStatus.BAD_REQUEST.value(), I18nContextHolder.get("job.init.error"));
    }

}
