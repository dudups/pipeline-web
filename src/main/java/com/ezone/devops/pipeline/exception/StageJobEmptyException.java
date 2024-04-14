package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class StageJobEmptyException extends BaseException {

    public StageJobEmptyException() {
        super(HttpStatus.BAD_REQUEST.value(), "阶段下的任务不能为空");
    }

}
