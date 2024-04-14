package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class NoPermissionOperateException extends BaseException {

    public NoPermissionOperateException() {
        super(HttpStatus.FORBIDDEN.value(), "没有权限操作");
    }

}
