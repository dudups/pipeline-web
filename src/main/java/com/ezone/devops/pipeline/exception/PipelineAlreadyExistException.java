package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class PipelineAlreadyExistException extends BaseException {

    public PipelineAlreadyExistException(String pipelineName) {
        super(HttpStatus.CONFLICT.value(), "流水线名称" + pipelineName + "已经存在");
    }

}
