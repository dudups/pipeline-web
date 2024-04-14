package com.ezone.devops.plugins.job.deploy.ezk8s;

import com.ezone.devops.pipeline.clients.EzK8sClient;
import com.ezone.devops.pipeline.clients.request.DeployTaskPayload;
import com.ezone.devops.pipeline.clients.response.Ezk8sDeployTask;
import com.ezone.devops.pipeline.clients.response.Ezk8sDeployTaskResponse;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployBuild;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployConfig;
import com.ezone.devops.plugins.job.deploy.ezk8s.service.Ezk8sDeployBuildService;
import com.ezone.devops.plugins.job.deploy.ezk8s.service.Ezk8sDeployConfigService;
import com.ezone.devops.plugins.job.enums.VersionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class Ezk8sDeployOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String TASK_DASHBOARD_URL = "/ezK8S/clusters/%s/deploy/%s/task/%s";

    @Autowired
    private Ezk8sDeployBuildService ezk8SDeployBuildService;
    @Autowired
    private Ezk8sDeployConfigService ezk8SDeployConfigService;
    @Autowired
    private EzK8sClient ezK8sClient;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        log.info("start execute job:[{}]", jobRecord);
        Ezk8sDeployConfig ezk8SDeployConfig = ezk8SDeployConfigService.findById(jobRecord.getPluginId());
        Ezk8sDeployBuild ezk8SDeployBuild = ezk8SDeployBuildService.getById(jobRecord.getPluginRecordId());
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        String dockerTag;
        if (ezk8SDeployConfig.getVersionType() == VersionType.SNAPSHOT) {
            dockerTag = pipelineRecord.getSnapshotVersion();
        } else if (ezk8SDeployConfig.getVersionType() == VersionType.RELEASE) {
            String releaseVersion = pipelineRecord.getReleaseVersion();
            if (StringUtils.isEmpty(releaseVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "这条流水线尚未发布版本");
                return false;
            }
            dockerTag = pipelineRecord.getReleaseVersion();
        } else {
            String customVersion = FreemarkerUtil.parseExpression(ezk8SDeployConfig.getCustomVersion(), envs);
            if (StringUtils.isEmpty(customVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "解析版本号出错");
                return false;
            }
            dockerTag = customVersion;
        }

        DeployTaskPayload deployTaskPayload = new DeployTaskPayload(repo.getCompanyId(), jobRecord.getTriggerUser(),
                dockerTag, ezk8SDeployConfig, ezk8SDeployBuild);
        Ezk8sDeployTaskResponse response = ezK8sClient.createDeployTask(ezk8SDeployConfig.getClusterName(),
                ezk8SDeployConfig.getEnvName(), deployTaskPayload);
        if (response == null) {
            log.info("invoke ezk8s error,response is null job:[{}]", jobRecord);
            invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, ezK8sClient.getPlatformName());
            return false;
        }

        if (response.isError()) {
            log.info("invoke ezk8s error response:[{}]", response);
            super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
            return false;
        }

        // 通知插件执行成功
        String dashboardUrl = generateDashboardUrl(response.getData());
        ezk8SDeployBuild.setDashboardUrl(dashboardUrl);
        ezk8SDeployBuildService.updateBuild(ezk8SDeployBuild);
        super.noticeJobSuccess(pipelineRecord, jobRecord, response.getMessage());
        return true;
    }

    private String generateDashboardUrl(Ezk8sDeployTask ezk8sDeployTask) {
        return String.format(TASK_DASHBOARD_URL, ezk8sDeployTask.getClusterId(), ezk8sDeployTask.getEnvId(),
                ezk8sDeployTask.getId());
    }
}