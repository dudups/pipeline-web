package com.ezone.devops.plugins.job.build.cmake.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.cmake.docker.bean.CmakeDocker;
import com.ezone.devops.plugins.job.build.cmake.docker.model.CmakeDockerConfig;
import com.ezone.devops.plugins.job.build.cmake.docker.service.CmakeDockerConfigService;
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
public class CmakeDockerDataOperator implements PluginDataOperator<CmakeDocker, EmptyBuild> {

    @Autowired
    private CmakeDockerConfigService cmakeDockerConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        CmakeDocker cmakeDocker = json.toJavaObject(CmakeDocker.class);
        if (cmakeDocker == null) {
            return false;
        }

        if (cmakeDocker.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(cmakeDocker.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(cmakeDocker.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (cmakeDocker.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(cmakeDocker.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(cmakeDocker.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(cmakeDocker.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(cmakeDocker.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (cmakeDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (cmakeDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (cmakeDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(cmakeDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (cmakeDocker.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (cmakeDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION || cmakeDocker.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (cmakeDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(cmakeDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        return true;
    }

    @Override
    public CmakeDocker getRealJob(Long id) {
        CmakeDockerConfig cmakeDockerConfig = cmakeDockerConfigService.getById(id);
        return JsonUtils.toObject(cmakeDockerConfig.getDataJson(), CmakeDocker.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        CmakeDocker cmakeDocker = json.toJavaObject(CmakeDocker.class);
        CmakeDockerConfig cmakeDockerConfig = new CmakeDockerConfig();
        cmakeDockerConfig.setDataJson(JsonUtils.toJson(cmakeDocker));
        cmakeDockerConfigService.saveConfig(cmakeDockerConfig);
        return cmakeDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return cmakeDockerConfigService.deleteById(id);
    }

}
