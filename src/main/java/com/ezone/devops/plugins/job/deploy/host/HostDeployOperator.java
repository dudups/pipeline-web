package com.ezone.devops.plugins.job.deploy.host;

import com.ezone.devops.pipeline.clients.EzDeployClient;
import com.ezone.devops.pipeline.clients.request.EzDeployTaskPayload;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.deploy.host.model.HostDeployBuild;
import com.ezone.devops.plugins.job.deploy.host.model.HostDeployConfig;
import com.ezone.devops.plugins.job.deploy.host.service.HostDeployBuildService;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class HostDeployOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String TASK_DASHBOARD_URL = "/ezDeploy/%s/task/%s";

    @Autowired
    private EzDeployClient ezDeployClient;
    @Autowired
    private HostDeployBuildService hostDeployBuildService;
    @Autowired
    private HostDeployDataOperator hostDeployDataOperator;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Long realJobBuildId = jobRecord.getPluginRecordId();
        HostDeployBuild hostDeployBuild = hostDeployDataOperator.getRealJobRecord(realJobBuildId);
        if (null == hostDeployBuild) {
            log.error("not found deploy build by id:[{}]", realJobBuildId);
            super.noticeJobFailed(pipelineRecord, jobRecord, I18nContextHolder.get("job.init.error"));
            return false;
        }

        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
        HostDeployConfig hostDeployConfig = hostDeployDataOperator.getRealJob(jobRecord.getPluginId());
        String version;
        if (hostDeployConfig.getVersionType() == VersionType.SNAPSHOT) {
            version = pipelineRecord.getSnapshotVersion();
        } else if (hostDeployConfig.getVersionType() == VersionType.RELEASE) {
            String releaseVersion = pipelineRecord.getReleaseVersion();
            if (StringUtils.isEmpty(releaseVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "这条流水线尚未发布版本");
                return false;
            }
            version = releaseVersion;
        } else {
            version = FreemarkerUtil.parseExpression(hostDeployBuild.getCustomVersion(), envs);
            if (StringUtils.isEmpty(version)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "解析版本号出错");
                return false;
            }
        }
        String deployMessage = hostDeployBuild.getDeployMessage();
        EzDeployTaskPayload body = new EzDeployTaskPayload();
        body.setTemplateId(hostDeployConfig.getTemplateId());
        body.setTriggerUser(jobRecord.getTriggerUser());
        body.setVersion(version);
        body.setDescription(hostDeployBuild.getDeployMessage());
        body.setCallbackUrl(getCallbackUrl(jobRecord));

        Set<EzDeployTaskPayload.VariablePayload> variables = Sets.newHashSetWithExpectedSize(envs.size());
        for (Map.Entry<String, String> entry : envs.entrySet()) {
            EzDeployTaskPayload.VariablePayload variable = new EzDeployTaskPayload.VariablePayload();
            variable.setVariableKey(entry.getKey());
            variable.setVariableValue(entry.getValue());
            variables.add(variable);
        }
        body.setVariables(variables);

        BaseResponse<?> response = ezDeployClient.createDeployTask(body);
        if (response == null) {
            log.info("invoke ezdeploy error,response is null job:[{}]", jobRecord);
            invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, ezDeployClient.getPlatformName());
            return false;
        }

        if (response.isError()) {
            log.info("invoke ezdeploy error response:[{}]", response);
            super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
            return false;
        }

        // 通知插件执行成功
        String dashboardUrl = generateDashboardUrl(response, hostDeployConfig);
        hostDeployBuild.setDeployMessage(StringUtils.defaultIfBlank(deployMessage, StringUtils.EMPTY));
        hostDeployBuild.setCustomVersion(version);
        hostDeployBuild.setDashboardUrl(dashboardUrl);
        hostDeployBuildService.updateBuild(hostDeployBuild);
        return true;
    }

    private String generateDashboardUrl(BaseResponse<?> response, HostDeployConfig hostDeployConfig) {
        return String.format(TASK_DASHBOARD_URL, hostDeployConfig.getHostGroupId(), response.getData());
    }
}