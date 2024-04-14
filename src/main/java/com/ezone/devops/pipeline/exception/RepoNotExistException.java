package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class RepoNotExistException extends BaseException {

    public RepoNotExistException() {
        super(HttpStatus.NOT_FOUND.value(), "代码库不存在");
    }

}
