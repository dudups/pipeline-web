package com.ezone.devops.plugins.job.other.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorBuild;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorConfig;
import com.ezone.devops.plugins.job.other.docker.service.DockerExecutorBuildService;
import com.ezone.devops.plugins.job.other.docker.service.DockerExecutorConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DockerExecutorDataOperator implements PluginDataOperator<DockerExecutorConfig, DockerExecutorBuild> {

    @Autowired
    private DockerExecutorBuildService dockerExecutorBuildService;
    @Autowired
    private DockerExecutorConfigService dockerExecutorConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        DockerExecutorConfig dockerExecutorConfig = json.toJavaObject(DockerExecutorConfig.class);
        if (dockerExecutorConfig == null) {
            return false;
        }
        if (StringUtils.isBlank(dockerExecutorConfig.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的命令不能为空");
        }

        if (dockerExecutorConfig.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (dockerExecutorConfig.isUploadReport()) {
            if (StringUtils.isBlank(dockerExecutorConfig.getReportDir())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的报告目录不允许为空");
            }
            if (StringUtils.isBlank(dockerExecutorConfig.getReportIndex())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的报告访问入口不允许为空");
            }
        }

        if (dockerExecutorConfig.isUploadArtifact()) {
            if (StringUtils.isBlank(dockerExecutorConfig.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }
            if (StringUtils.isBlank(dockerExecutorConfig.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }
            if (StringUtils.isBlank(dockerExecutorConfig.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (dockerExecutorConfig.isPushImage()) {
            if (StringUtils.isBlank(dockerExecutorConfig.getDockerRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
            }
            if (StringUtils.isBlank(dockerExecutorConfig.getDockerImageName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
            }
            if (StringUtils.isBlank(dockerExecutorConfig.getDockerfile())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的Dockerfile不允许为空");
            }
            if (StringUtils.isBlank(dockerExecutorConfig.getDockerContext())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker context不允许为空");
            }
            if (dockerExecutorConfig.getDockerVersionType() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker版本类型不允许为空");
            }
            if (dockerExecutorConfig.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(dockerExecutorConfig.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }

        }
        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        DockerExecutorConfig dockerExecutorConfig = json.toJavaObject(DockerExecutorConfig.class);
        dockerExecutorConfig = dockerExecutorConfigService.saveDockerExecutorConfig(dockerExecutorConfig);
        return dockerExecutorConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return dockerExecutorConfigService.deleteDockerExecutorConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        DockerExecutorBuild dockerExecutorBuild = json.toJavaObject(DockerExecutorBuild.class);
        return dockerExecutorBuildService.updateDockerExecutorBuild(dockerExecutorBuild);
    }

    @Override
    public DockerExecutorConfig getRealJob(Long id) {
        return dockerExecutorConfigService.getById(id);
    }

    @Override
    public DockerExecutorBuild getRealJobRecord(Long id) {
        return dockerExecutorBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        DockerExecutorConfig dockerExecutorConfig = getRealJob(realJobConfigId);
        if (null == dockerExecutorConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        DockerExecutorBuild dockerExecutorBuild = new DockerExecutorBuild();
        dockerExecutorBuild = dockerExecutorBuildService.saveDockerExecutorBuild(dockerExecutorBuild);
        log.debug("save build:[{}]", dockerExecutorBuild);
        return dockerExecutorBuild.getId();
    }
}
