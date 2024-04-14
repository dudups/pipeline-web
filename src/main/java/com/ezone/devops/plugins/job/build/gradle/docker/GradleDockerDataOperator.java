package com.ezone.devops.plugins.job.build.gradle.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.build.gradle.docker.bean.GradleDockerBuildBean;
import com.ezone.devops.plugins.job.build.gradle.docker.bean.GradleDockerConfigBean;
import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerBuild;
import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerConfig;
import com.ezone.devops.plugins.job.build.gradle.docker.service.GradleDockerBuildService;
import com.ezone.devops.plugins.job.build.gradle.docker.service.GradleDockerConfigService;
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
public class GradleDockerDataOperator implements PluginDataOperator<GradleDockerConfigBean, GradleDockerBuildBean> {

    @Autowired
    private GradleDockerConfigService gradleDockerConfigService;
    @Autowired
    private GradleDockerBuildService gradleDockerBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        GradleDockerConfigBean gradleDockerConfigBean = json.toJavaObject(GradleDockerConfigBean.class);
        if (gradleDockerConfigBean == null) {
            return false;
        }

        if (gradleDockerConfigBean.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(gradleDockerConfigBean.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(gradleDockerConfigBean.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (gradleDockerConfigBean.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(gradleDockerConfigBean.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(gradleDockerConfigBean.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(gradleDockerConfigBean.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(gradleDockerConfigBean.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (gradleDockerConfigBean.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (gradleDockerConfigBean.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (gradleDockerConfigBean.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(gradleDockerConfigBean.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (gradleDockerConfigBean.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (gradleDockerConfigBean.getDockerVersionType() == VersionType.DEFAULT_VERSION || gradleDockerConfigBean.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (gradleDockerConfigBean.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(gradleDockerConfigBean.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        if (gradleDockerConfigBean.isEnableScan()) {
            if (StringUtils.isBlank(gradleDockerConfigBean.getOutputPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描路径不能为空");
            }

            if (gradleDockerConfigBean.getRulesetId() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描规则集不能为空");
            }

            if (gradleDockerConfigBean.getScanLevel() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "扫描级别不能为空");
            }
        }

        return true;
    }

    @Override
    public GradleDockerConfigBean getRealJob(Long id) {
        GradleDockerConfig gradleDockerConfig = gradleDockerConfigService.getById(id);
        return JsonUtils.toObject(gradleDockerConfig.getDataJson(), GradleDockerConfigBean.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        GradleDockerConfigBean gradleDockerConfigBean = json.toJavaObject(GradleDockerConfigBean.class);
        GradleDockerConfig gradleDockerConfig = new GradleDockerConfig();
        gradleDockerConfig.setDataJson(JsonUtils.toJson(gradleDockerConfigBean));
        gradleDockerConfigService.saveConfig(gradleDockerConfig);
        return gradleDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return gradleDockerConfigService.deleteById(id);
    }


    @Override
    public GradleDockerBuildBean getRealJobRecord(Long id) {
        GradleDockerBuild gradleDockerBuild = gradleDockerBuildService.getById(id);
        if (gradleDockerBuild == null) {
            return null;
        }

        return JsonUtils.toObject(gradleDockerBuild.getDataJson(), GradleDockerBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        GradleDockerConfigBean gradleDockerConfigBean = getRealJob(realJobConfigId);
        GradleDockerBuildBean gradleDockerBuildBean = new GradleDockerBuildBean();
        gradleDockerBuildBean.setEnableScan(gradleDockerConfigBean.isEnableScan());

        GradleDockerBuild gradleDockerBuild = new GradleDockerBuild();
        gradleDockerBuild.setDataJson(JsonUtils.toJson(gradleDockerBuildBean));

        gradleDockerBuildService.saveBuild(gradleDockerBuild);
        return gradleDockerBuild.getId();
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject buildJson) {
        GradleDockerBuildBean gradleDockerBuildBean = buildJson.toJavaObject(GradleDockerBuildBean.class);
        GradleDockerBuild gradleDockerBuild = gradleDockerBuildService.getById(realJobRecordId);
        gradleDockerBuild.setId(realJobRecordId);
        gradleDockerBuild.setDataJson(JsonUtils.toJson(gradleDockerBuildBean));
        return gradleDockerBuildService.updateBuild(gradleDockerBuild);
    }
}
