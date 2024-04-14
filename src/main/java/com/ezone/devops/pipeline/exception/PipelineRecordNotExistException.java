package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class PipelineRecordNotExistException extends BaseException {

    public PipelineRecordNotExistException() {
        super(HttpStatus.NOT_FOUND.value(), "流水线构建记录不存在");
    }

}
