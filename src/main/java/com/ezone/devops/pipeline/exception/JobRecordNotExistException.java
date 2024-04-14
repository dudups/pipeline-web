package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class JobRecordNotExistException extends BaseException {

    public JobRecordNotExistException() {
        super(HttpStatus.NOT_FOUND.value(), "任务记录不存在");
    }

}
