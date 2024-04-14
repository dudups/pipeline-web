package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class VersionAlreadyExistException extends BaseException {

    public VersionAlreadyExistException() {
        super(HttpStatus.CONFLICT.value(), "版本号已经存在");
    }

}
