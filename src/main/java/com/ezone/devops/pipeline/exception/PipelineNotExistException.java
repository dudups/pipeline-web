package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class PipelineNotExistException extends BaseException {

    public PipelineNotExistException() {
        super(HttpStatus.NOT_FOUND.value(), "流水线不存在");
    }

}
