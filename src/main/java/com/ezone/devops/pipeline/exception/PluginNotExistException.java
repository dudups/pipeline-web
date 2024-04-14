package com.ezone.devops.pipeline.exception;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.springframework.http.HttpStatus;

public class PluginNotExistException extends BaseException {

    public PluginNotExistException(String pluginType) {
        super(HttpStatus.NOT_FOUND.value(), "插件" + pluginType + "不存在");
    }

}
