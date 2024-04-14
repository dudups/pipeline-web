package com.ezone.devops.plugins.job.build.python.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.python.docker.bean.PythonDocker;
import com.ezone.devops.plugins.job.build.python.docker.model.PythonDockerConfig;
import com.ezone.devops.plugins.job.build.python.docker.service.PythonDockerConfigService;
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
public class PythonDockerDataOperator implements PluginDataOperator<PythonDocker, EmptyBuild> {

    @Autowired
    private PythonDockerConfigService pythonDockerConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        PythonDocker pythonDocker = json.toJavaObject(PythonDocker.class);
        if (pythonDocker == null) {
            return false;
        }

        if (pythonDocker.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(pythonDocker.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(pythonDocker.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (pythonDocker.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(pythonDocker.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(pythonDocker.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(pythonDocker.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(pythonDocker.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (pythonDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (pythonDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (pythonDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(pythonDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (pythonDocker.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (pythonDocker.getDockerVersionType() == VersionType.DEFAULT_VERSION || pythonDocker.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (pythonDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(pythonDocker.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        if (pythonDocker.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(pythonDocker.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥文件生成路径不能为空");
            }
            if (StringUtils.equals(pythonDocker.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(pythonDocker.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
//            if (CollectionUtils.isEmpty(pythonDocker.getPublicRepoNames())) {
//                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的镜像仓库名称不能为空");
//            }
        }

        return true;
    }

    @Override
    public PythonDocker getRealJob(Long id) {
        PythonDockerConfig pythonDockerConfig = pythonDockerConfigService.getById(id);
        return JsonUtils.toObject(pythonDockerConfig.getDataJson(), PythonDocker.class);
    }


    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        PythonDocker pythonDocker = json.toJavaObject(PythonDocker.class);
        PythonDockerConfig pythonDockerConfig = new PythonDockerConfig();
        pythonDockerConfig.setDataJson(JsonUtils.toJson(pythonDocker));
        pythonDockerConfigService.saveConfig(pythonDockerConfig);
        return pythonDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return pythonDockerConfigService.deleteById(id);
    }

}
