package com.ezone.devops.plugins.job.build.go.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.go.docker.bean.GoDocker;
import com.ezone.devops.plugins.job.build.go.docker.model.GoDockerConfig;
import com.ezone.devops.plugins.job.build.go.docker.service.GoDockerConfigService;
import com.ezone.devops.plugins.job.enums.RegistryType;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoDockerDataOperator implements PluginDataOperator<GoDocker, EmptyBuild> {

    @Autowired
    private GoDockerConfigService goDockerConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        GoDocker goDocker = json.toJavaObject(GoDocker.class);
        if (goDocker == null) {
            return false;
        }

        if (goDocker.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(goDocker.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(goDocker.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (goDocker.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(goDocker.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(goDocker.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(goDocker.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(goDocker.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (goDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (goDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (goDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(goDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (goDocker.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (goDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION || goDocker.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (goDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(goDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        return true;
    }

    @Override
    public GoDocker getRealJob(Long id) {
        GoDockerConfig goDockerConfig = goDockerConfigService.getById(id);
        return JsonUtils.toObject(goDockerConfig.getDataJson(), GoDocker.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        GoDocker goDocker = json.toJavaObject(GoDocker.class);
        GoDockerConfig goDockerConfig = new GoDockerConfig();
        goDockerConfig.setDataJson(JsonUtils.toJson(goDocker));
        goDockerConfigService.saveConfig(goDockerConfig);
        return goDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return goDockerConfigService.deleteById(id);
    }

}
