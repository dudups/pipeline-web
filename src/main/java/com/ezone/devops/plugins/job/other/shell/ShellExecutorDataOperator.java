package com.ezone.devops.plugins.job.other.shell;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.other.shell.model.ShellExecutorConfig;
import com.ezone.devops.plugins.job.other.shell.service.ShellExecutorConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShellExecutorDataOperator implements PluginDataOperator<ShellExecutorConfig, EmptyBuild> {

    @Autowired
    private ShellExecutorConfigService shellExecutorConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        ShellExecutorConfig shellExecutorConfig = json.toJavaObject(ShellExecutorConfig.class);
        if (shellExecutorConfig == null) {
            return false;
        }
        if (shellExecutorConfig.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (shellExecutorConfig.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.isBlank(shellExecutorConfig.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的命令不能为空");
        }

        return true;
    }


    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        ShellExecutorConfig shellExecutorConfig = json.toJavaObject(ShellExecutorConfig.class);
        shellExecutorConfig = shellExecutorConfigService.saveShellExecutorConfig(shellExecutorConfig);
        return shellExecutorConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return shellExecutorConfigService.deleteShellExecutorConfig(id);
    }

    @Override
    public ShellExecutorConfig getRealJob(Long id) {
        return shellExecutorConfigService.getById(id);
    }
}
