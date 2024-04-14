package com.ezone.devops.plugins.job.build.npm.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.npm.docker.bean.NpmDocker;
import com.ezone.devops.plugins.job.build.npm.docker.model.NpmDockerConfig;
import com.ezone.devops.plugins.job.build.npm.docker.service.NpmDockerConfigService;
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
public class NpmDockerDataOperator implements PluginDataOperator<NpmDocker, EmptyBuild> {

    @Autowired
    private NpmDockerConfigService npmDockerConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        NpmDocker npmDocker = json.toJavaObject(NpmDocker.class);
        if (npmDocker == null) {
            return false;
        }

        if (npmDocker.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(npmDocker.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(npmDocker.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (npmDocker.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(npmDocker.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(npmDocker.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(npmDocker.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(npmDocker.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (npmDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (npmDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (npmDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(npmDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (npmDocker.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (npmDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION || npmDocker.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (npmDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(npmDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        if (npmDocker.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(npmDocker.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥文件生成路径不能为空");
            }
            if (StringUtils.equals(npmDocker.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(npmDocker.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
//            if (CollectionUtils.isEmpty(npmDocker.getPublicRepoNames())) {
//                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的镜像仓库名称不能为空");
//            }
        }

        return true;
    }

    @Override
    public NpmDocker getRealJob(Long id) {
        NpmDockerConfig npmDockerConfig = npmDockerConfigService.getById(id);
        return JsonUtils.toObject(npmDockerConfig.getDataJson(), NpmDocker.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        NpmDocker npmDocker = json.toJavaObject(NpmDocker.class);
        NpmDockerConfig npmDockerConfig = new NpmDockerConfig();
        npmDockerConfig.setDataJson(JsonUtils.toJson(npmDocker));
        npmDockerConfigService.saveConfig(npmDockerConfig);
        return npmDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return npmDockerConfigService.deleteById(id);
    }

}
