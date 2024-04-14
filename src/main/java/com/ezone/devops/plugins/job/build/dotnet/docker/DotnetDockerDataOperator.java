package com.ezone.devops.plugins.job.build.dotnet.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.dotnet.docker.bean.DotnetDocker;
import com.ezone.devops.plugins.job.build.dotnet.docker.model.DotnetDockerConfig;
import com.ezone.devops.plugins.job.build.dotnet.docker.service.DotnetDockerConfigService;
import com.ezone.devops.plugins.job.enums.RegistryType;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DotnetDockerDataOperator implements PluginDataOperator<DotnetDocker, EmptyBuild> {

    @Autowired
    private DotnetDockerConfigService dotnetDockerConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        DotnetDocker dotnetDocker = json.toJavaObject(DotnetDocker.class);
        if (dotnetDocker == null) {
            return false;
        }

        if (dotnetDocker.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(dotnetDocker.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(dotnetDocker.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (dotnetDocker.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(dotnetDocker.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(dotnetDocker.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(dotnetDocker.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(dotnetDocker.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (dotnetDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (dotnetDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (dotnetDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(dotnetDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (dotnetDocker.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (dotnetDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION || dotnetDocker.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (dotnetDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(dotnetDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        if (dotnetDocker.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(dotnetDocker.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥生成路径不能为空");
            }
            if (StringUtils.equals(dotnetDocker.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(dotnetDocker.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
//            if (CollectionUtils.isEmpty(dotnetDocker.getPublicRepoNames())) {
//                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的镜像仓库名称不能为空");
//            }
        }

        return true;
    }

    @Override
    public DotnetDocker getRealJob(Long id) {
        DotnetDockerConfig dotnetDockerConfig = dotnetDockerConfigService.getById(id);
        return JsonUtils.toObject(dotnetDockerConfig.getDataJson(), DotnetDocker.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        DotnetDocker dotnetDocker = json.toJavaObject(DotnetDocker.class);
        DotnetDockerConfig dotnetDockerConfig = new DotnetDockerConfig();
        dotnetDockerConfig.setDataJson(JsonUtils.toJson(dotnetDocker));
        dotnetDockerConfigService.saveConfig(dotnetDockerConfig);
        return dotnetDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return dotnetDockerConfigService.deleteById(id);
    }

}
