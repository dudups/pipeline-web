package com.ezone.devops.plugins.job.build.php.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.php.docker.bean.PhpDocker;
import com.ezone.devops.plugins.job.build.php.docker.model.PhpDockerConfig;
import com.ezone.devops.plugins.job.build.php.docker.service.PhpDockerConfigService;
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
public class PhpDockerDataOperator implements PluginDataOperator<PhpDocker, EmptyBuild> {

    @Autowired
    private PhpDockerConfigService phpDockerConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        PhpDocker phpDocker = json.toJavaObject(PhpDocker.class);
        if (phpDocker == null) {
            return false;
        }

        if (phpDocker.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(phpDocker.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(phpDocker.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (phpDocker.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(phpDocker.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(phpDocker.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(phpDocker.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(phpDocker.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (phpDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (phpDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (phpDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(phpDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (phpDocker.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (phpDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION || phpDocker.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (phpDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(phpDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        if (phpDocker.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(phpDocker.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥文件生成路径不能为空");
            }
            if (StringUtils.equals(phpDocker.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(phpDocker.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
//            if (CollectionUtils.isEmpty(phpDocker.getPublicRepoNames())) {
//                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的镜像仓库名称不能为空");
//            }
        }

        return true;
    }

    @Override
    public PhpDocker getRealJob(Long id) {
        PhpDockerConfig phpDockerConfig = phpDockerConfigService.getById(id);
        return JsonUtils.toObject(phpDockerConfig.getDataJson(), PhpDocker.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        PhpDocker phpDocker = json.toJavaObject(PhpDocker.class);
        PhpDockerConfig phpDockerConfig = new PhpDockerConfig();
        phpDockerConfig.setDataJson(JsonUtils.toJson(phpDocker));
        phpDockerConfigService.saveConfig(phpDockerConfig);
        return phpDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return phpDockerConfigService.deleteById(id);
    }

}
