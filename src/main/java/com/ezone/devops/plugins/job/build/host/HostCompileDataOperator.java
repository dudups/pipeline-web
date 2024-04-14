package com.ezone.devops.plugins.job.build.host;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.build.host.model.HostCompileBuild;
import com.ezone.devops.plugins.job.build.host.model.HostCompileConfig;
import com.ezone.devops.plugins.job.build.host.service.HostCompileBuildService;
import com.ezone.devops.plugins.job.build.host.service.HostCompileConfigService;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HostCompileDataOperator implements PluginDataOperator<HostCompileConfig, HostCompileBuild> {

    @Autowired
    private HostCompileConfigService hostCompileConfigService;
    @Autowired
    private HostCompileBuildService hostCompileBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        HostCompileConfig hostCompileConfig = json.toJavaObject(HostCompileConfig.class);
        if (StringUtils.isBlank(hostCompileConfig.getBuildCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的命令不能为空");
        }

        if (hostCompileConfig.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(hostCompileConfig.getBuildCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (hostCompileConfig.isUploadArtifact()) {
            if (StringUtils.isBlank(hostCompileConfig.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }
            if (StringUtils.isBlank(hostCompileConfig.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }
            if (StringUtils.isBlank(hostCompileConfig.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (hostCompileConfig.isUploadReport()) {
            if (StringUtils.isBlank(hostCompileConfig.getReportDir())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的报告目录不允许为空");
            }
            if (StringUtils.isBlank(hostCompileConfig.getReportIndex())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的报告访问入口不允许为空");
            }
        }

        if (hostCompileConfig.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(hostCompileConfig.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }
        return true;
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        HostCompileBuild hostCompileBuild = new HostCompileBuild();
        hostCompileBuildService.saveBuild(hostCompileBuild);
        return hostCompileBuild.getId();
    }

    @Override
    public HostCompileConfig getRealJob(Long id) {
        return hostCompileConfigService.getById(id);
    }

    @Override
    public HostCompileBuild getRealJobRecord(Long id) {
        return hostCompileBuildService.getById(id);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        HostCompileConfig hostCompileConfig = json.toJavaObject(HostCompileConfig.class);
        hostCompileConfigService.saveConfig(hostCompileConfig);
        return hostCompileConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return hostCompileConfigService.deleteById(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        HostCompileBuild hostCompileBuild = json.toJavaObject(HostCompileBuild.class);
        return hostCompileBuildService.updateBuild(hostCompileBuild);
    }

}
