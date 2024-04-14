package com.ezone.devops.plugins.job.build.gradle.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.build.gradle.file.bean.GradleBuildBean;
import com.ezone.devops.plugins.job.build.gradle.file.bean.GradleConfigBean;
import com.ezone.devops.plugins.job.build.gradle.file.model.GradleBuild;
import com.ezone.devops.plugins.job.build.gradle.file.model.GradleConfig;
import com.ezone.devops.plugins.job.build.gradle.file.service.GradleBuildService;
import com.ezone.devops.plugins.job.build.gradle.file.service.GradleConfigService;
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
public class GradleDataOperator implements PluginDataOperator<GradleConfigBean, GradleBuildBean> {

    @Autowired
    private GradleConfigService gradleConfigService;
    @Autowired
    private GradleBuildService gradleBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        GradleConfigBean gradleConfigBean = json.toJavaObject(GradleConfigBean.class);
        if (gradleConfigBean == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (gradleConfigBean.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(gradleConfigBean.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (gradleConfigBean.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (gradleConfigBean.isUploadArtifact()) {
            if (StringUtils.isBlank(gradleConfigBean.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(gradleConfigBean.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(gradleConfigBean.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (gradleConfigBean.isEnableScan()) {
            if (StringUtils.isBlank(gradleConfigBean.getOutputPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描路径不能为空");
            }

            if (gradleConfigBean.getRulesetId() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描规则集不能为空");
            }

            if (gradleConfigBean.getScanLevel() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "扫描级别不能为空");
            }
        }

        if (gradleConfigBean.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(gradleConfigBean.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public GradleConfigBean getRealJob(Long id) {
        GradleConfig gradleConfig = gradleConfigService.getById(id);
        return JsonUtils.toObject(gradleConfig.getDataJson(), GradleConfigBean.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        GradleConfigBean gradleConfigBean = json.toJavaObject(GradleConfigBean.class);
        GradleConfig gradleConfig = new GradleConfig();
        gradleConfig.setDataJson(JsonUtils.toJson(gradleConfigBean));
        gradleConfigService.saveConfig(gradleConfig);
        return gradleConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return gradleConfigService.deleteById(id);
    }


    @Override
    public GradleBuildBean getRealJobRecord(Long id) {
        GradleBuild gradleBuild = gradleBuildService.getById(id);
        if (gradleBuild == null) {
            return null;
        }
        return JsonUtils.toObject(gradleBuild.getDataJson(), GradleBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        GradleConfigBean gradleConfigBean = getRealJob(realJobConfigId);
        GradleBuildBean gradleBuildBean = new GradleBuildBean();
        gradleBuildBean.setEnableScan(gradleConfigBean.isEnableScan());

        GradleBuild gradleBuild = new GradleBuild();
        gradleBuild.setDataJson(JsonUtils.toJson(gradleBuildBean));

        gradleBuildService.saveBuild(gradleBuild);
        return gradleBuild.getId();
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject buildJson) {
        GradleBuildBean gradleBuildBean = buildJson.toJavaObject(GradleBuildBean.class);
        GradleBuild gradleBuild = gradleBuildService.getById(realJobRecordId);
        gradleBuild.setId(realJobRecordId);
        gradleBuild.setDataJson(JsonUtils.toJson(gradleBuildBean));
        return gradleBuildService.updateBuild(gradleBuild);
    }
}
