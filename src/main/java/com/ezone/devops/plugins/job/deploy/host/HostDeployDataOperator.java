package com.ezone.devops.plugins.job.deploy.host;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.ezcode.sdk.bean.model.InternalCommit;
import com.ezone.devops.ezcode.sdk.service.InternalCommitService;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.deploy.host.model.HostDeployBuild;
import com.ezone.devops.plugins.job.deploy.host.model.HostDeployConfig;
import com.ezone.devops.plugins.job.deploy.host.service.HostDeployBuildService;
import com.ezone.devops.plugins.job.deploy.host.service.HostDeployConfigService;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class HostDeployDataOperator implements PluginDataOperator<HostDeployConfig, HostDeployBuild> {

    @Autowired
    private InternalCommitService commitService;
    @Autowired
    private HostDeployBuildService hostDeployBuildService;
    @Autowired
    private HostDeployConfigService hostDeployConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        HostDeployConfig hostDeployConfig = json.toJavaObject(HostDeployConfig.class);
        if (hostDeployConfig.getHostGroupId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的命令不能为空");
        }

        if (hostDeployConfig.getTemplateId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的部署模板不能为空");
        }

        if (hostDeployConfig.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(hostDeployConfig.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不能为空");
            }
        }
        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        HostDeployConfig hostDeployConfig = json.toJavaObject(HostDeployConfig.class);
        hostDeployConfigService.saveConfig(hostDeployConfig);
        return hostDeployConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return hostDeployConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        HostDeployBuild hostDeployBuild = json.toJavaObject(HostDeployBuild.class);
        return hostDeployBuildService.updateBuild(hostDeployBuild);
    }

    @Override
    public HostDeployConfig getRealJob(Long id) {
        return hostDeployConfigService.getById(id);
    }

    @Override
    public HostDeployBuild getRealJobRecord(Long id) {
        return hostDeployBuildService.getById(id);
    }

    @Override
    public boolean updateRealJobBuildWithFields(Long realJobRecordId, JSONObject updateFields) {
        if (null == updateFields) {
            return false;
        }

        HostDeployBuild feHostDeployBuild = updateFields.toJavaObject(HostDeployBuild.class);
        if (null == feHostDeployBuild) {
            return false;
        }
        String deployMessage = feHostDeployBuild.getDeployMessage();
        if (StringUtils.isBlank(deployMessage)) {
            return false;
        }

        if (StringUtils.length(deployMessage) > 2000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "部署说明最长不得超过2000个字符");
        }

        HostDeployBuild hostDeployBuild = getRealJobRecord(realJobRecordId);
        hostDeployBuild.setDeployMessage(deployMessage);
        hostDeployBuildService.updateBuild(hostDeployBuild);
        return true;
    }


    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        RepoVo repo = context.getRepo();
        PipelineRecord pipelineRecord = context.getPipelineRecord();
        InternalCommit commit = commitService.getCommit(repo.getCompanyId(), repo.getRepoKey(), pipelineRecord.getCommitId());

        HostDeployConfig hostDeployConfig = hostDeployConfigService.getById(realJobConfigId);
        HostDeployBuild hostDeployBuild = new HostDeployBuild();
        hostDeployBuild.setGroupId(hostDeployConfig.getHostGroupId());
        hostDeployBuild.setTemplateId(hostDeployConfig.getTemplateId());
        hostDeployBuild.setVersionType(hostDeployConfig.getVersionType());
        hostDeployBuild.setCustomVersion(StringUtils.defaultString(hostDeployConfig.getCustomVersion(), StringUtils.EMPTY));

        if (Optional.ofNullable(commit).map(InternalCommit::getMessage).isPresent()) {
            hostDeployBuild.setDeployMessage(commit.getMessage());
        } else {
            hostDeployBuild.setDeployMessage(StringUtils.EMPTY);
        }

        hostDeployBuildService.saveBuild(hostDeployBuild);
        log.debug("save build:[{}]", hostDeployBuild);
        return hostDeployBuild.getId();
    }
}
