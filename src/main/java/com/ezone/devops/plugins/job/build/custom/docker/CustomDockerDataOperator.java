package com.ezone.devops.plugins.job.build.custom.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.custom.docker.bean.CustomDocker;
import com.ezone.devops.plugins.job.build.custom.docker.model.CustomDockerConfig;
import com.ezone.devops.plugins.job.build.custom.docker.service.CustomDockerConfigService;
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
public class CustomDockerDataOperator implements PluginDataOperator<CustomDocker, EmptyBuild> {

    @Autowired
    private CustomDockerConfigService customDockerConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        CustomDocker customDocker = json.toJavaObject(CustomDocker.class);
        if (customDocker == null) {
            return false;
        }

        if (customDocker.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(customDocker.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(customDocker.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (customDocker.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(customDocker.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(customDocker.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(customDocker.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(customDocker.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (customDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (customDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (customDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(customDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (customDocker.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (customDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION || customDocker.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (customDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(customDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        return true;
    }

    @Override
    public CustomDocker getRealJob(Long id) {
        CustomDockerConfig customDockerConfig = customDockerConfigService.getById(id);
        return JsonUtils.toObject(customDockerConfig.getDataJson(), CustomDocker.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        CustomDocker customDocker = json.toJavaObject(CustomDocker.class);
        CustomDockerConfig customDockerConfig = new CustomDockerConfig();
        customDockerConfig.setDataJson(JsonUtils.toJson(customDocker));
        customDockerConfigService.saveConfig(customDockerConfig);
        return customDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return customDockerConfigService.deleteById(id);
    }

}
