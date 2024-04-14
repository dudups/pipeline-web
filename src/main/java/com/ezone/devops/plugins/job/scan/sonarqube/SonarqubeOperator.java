package com.ezone.devops.plugins.job.scan.sonarqube;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.ezcode.sdk.service.InternalScanService;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.scan.sonarqube.enums.MetricLevel;
import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeBuild;
import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeConfig;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.SonarqubeStep;
import com.ezone.ezbase.iam.bean.enums.ServiceType;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
public class SonarqubeOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private SonarqubeDataOperator sonarqubeDataOperator;
    @Autowired
    private InternalScanService internalScanService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Long companyId = repo.getCompanyId();
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        SonarqubeConfig sonarqubeConfig = sonarqubeDataOperator.getRealJob(jobRecord.getPluginId());
        String providerName = sonarqubeConfig.getProviderName();
        log.info("start request sonarqube config:[{}]", jobRecord);
        BaseResponse<Object> configResponse = integrationService.queryDetail(companyId, ServiceType.SONARQUBE, providerName);
        if (configResponse == null) {
            log.info("request sonarqube config error:[{}]", jobRecord);
            super.invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, "integration");
            return false;
        }

        if (configResponse.isError()) {
            log.info("request sonarqube config error:[{}],response is:[{}]", jobRecord, configResponse);
            super.noticeJobFailed(pipelineRecord, jobRecord, configResponse.getMessage());
            return false;
        }

        com.ezone.ezbase.iam.bean.config.SonarqubeConfig config =
                (com.ezone.ezbase.iam.bean.config.SonarqubeConfig) configResponse.getData();

        BaseResponse<Boolean> hasSonarqubePermission = integrationService.checkPermission(jobRecord.getTriggerUser(), companyId, ServiceType.SONARQUBE, providerName);
        if (!hasSonarqubePermission.getData()) {
            super.noticeJobFailed(pipelineRecord, jobRecord, "没有使用SonarQube服务:" + providerName + "的权限");
            return false;
        }

        SonarqubeBuild sonarqubeBuild = sonarqubeDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        if (null == sonarqubeBuild) {
            super.noticeJobFailed(pipelineRecord, jobRecord, I18nContextHolder.get("job.init.error"));
            return false;
        }

        StringBuilder command = new StringBuilder();
        command.append("sonar-scanner");
        command.append(" -Dproject.settings=./sonar-project.properties");
        SonarqubeStep sonarqubeStep = new SonarqubeStep();

        String sonarHostUrl = config.getUrl();
        String sonarToken = config.getToken();

        sonarqubeStep.setSonarHostUrl(sonarHostUrl);
        sonarqubeStep.setToken(sonarToken);
        sonarqubeStep.setOverrideSonarConfig(sonarqubeConfig.isOverrideSonarConfig());
        sonarqubeStep.setEnableQualityControl(sonarqubeConfig.isEnableQualityControl());

        if (sonarqubeConfig.isOverrideSonarConfig()) {
            String content = FreemarkerUtil.parseExpression(sonarqubeConfig.getSonarProjectContent(), envs);
            if (content == null) {
                log.error("read sonar config error");
                super.noticeJobFailed(pipelineRecord, jobRecord, "read sonar config error");
                return false;
            }

            Properties properties = new Properties();
            try {
                properties.load(new StringReader(content));
            } catch (IOException e) {
                log.error("read sonar config error", e);
                super.noticeJobFailed(pipelineRecord, jobRecord, "read sonar config error");
                return false;
            }

            properties.setProperty("sonar.host.url", sonarHostUrl);
            if (StringUtils.isNotBlank(sonarToken)) {
                properties.setProperty("sonar.login", sonarToken);
            }

            StringWriter stringWriter = new StringWriter();
            try {
                properties.store(stringWriter, null);
            } catch (IOException e) {
                log.error("write sonar config error", e);
                super.noticeJobFailed(pipelineRecord, jobRecord, "write sonar config error");
                return false;
            }
            sonarqubeStep.setSonarProjectContent(stringWriter.toString());
        } else {
            command.append(" -Dsonar.host.url=").append(sonarHostUrl);
            if (StringUtils.isNotBlank(sonarToken)) {
                command.append(" -Dsonar.login=").append(sonarToken);
            }
        }

        CommonJob<SonarqubeStep> commonJob = new CommonJob<SonarqubeStep>()
                .setCloneMode(CloneMode.SINGLE_COMMIT)
                .setJobType(jobRecord.getJobType())
                .setCommand(command.toString())
                .setSteps(sonarqubeStep);

        ImageInfo buildImageInfo = new ImageInfo()
                .setAlias(ImageInfo.BUILD_NAME)
                .setName(getBuildImageUrl(sonarqubeConfig.getBuildImageId()))
                .setEntrypoint(Lists.newArrayList());

        RunnerJobInfo runnerJobInfo = createRunnerJob(repo, pipeline, jobRecord,
                pipeline.getResourceType(), pipeline.getClusterName(), ExecutorType.CONTAINER,
                buildImageInfo, envs, commonJob);

        if (runnerJobInfo.isSuccess()) {
            jobRecord.setTaskId(runnerJobInfo.getTaskId());
            jobRecord.setLogName(runnerJobInfo.getLogName());
        } else {
            super.noticeJobFailed(pipelineRecord, jobRecord, runnerJobInfo.getMessage());
        }

        return true;
    }

    @Override
    public void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        sendCancelJobLog(jobRecord, triggerUser);
        schedulerClient.cancelRunnerJob(jobRecord.getTaskId());
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message) {
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        SonarqubeConfig sonarqubeConfig = sonarqubeDataOperator.getRealJob(jobRecord.getPluginId());
        SonarqubeBuild sonarqubeBuild = sonarqubeDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        // 判断是否开启质量门禁
        if (sonarqubeConfig.isEnableQualityControl()) {
            int count;
            MetricLevel metricLevel = sonarqubeConfig.getMetricLevel();
            Integer bugsCount = NumberUtils.toInt(String.valueOf(sonarqubeBuild.getBugs()), 0);
            Integer vulnerabilitiesCount = NumberUtils.toInt(String.valueOf(sonarqubeBuild.getVulnerabilities()), 0);
            Integer codeSmellsCount = NumberUtils.toInt(String.valueOf(sonarqubeBuild.getCodeSmells()), 0);
            Integer securityHotspotCount = 0;
            if (sonarqubeBuild.isSupportedSecurityHotspot()) {
                securityHotspotCount = NumberUtils.toInt(String.valueOf(sonarqubeBuild.getSecurityHotspots()), 0);
            }
            switch (metricLevel) {
                case SECURITY_HOTSPOTS:
                    count = securityHotspotCount;
                    if (count > sonarqubeConfig.getGreaterThan()) {
                        jobFailedCallback(pipelineRecord, jobRecord, "问题数量超过质量门禁设置", data);
                    } else {
                        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                    }
                    break;
                case BUG: {
                    count = bugsCount + securityHotspotCount;
                    if (count > sonarqubeConfig.getGreaterThan()) {
                        jobFailedCallback(pipelineRecord, jobRecord, "问题数量超过质量门禁设置", data);
                    } else {
                        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                    }
                    break;
                }
                case VULNERABILITIES: {
                    count = bugsCount + vulnerabilitiesCount + securityHotspotCount;
                    if (count > sonarqubeConfig.getGreaterThan()) {
                        jobFailedCallback(pipelineRecord, jobRecord, "问题数量超过质量门禁设置", data);
                    } else {
                        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                    }
                    break;
                }
                default: {
                    count = bugsCount + vulnerabilitiesCount + codeSmellsCount + securityHotspotCount;
                    if (count > sonarqubeConfig.getGreaterThan()) {
                        jobFailedCallback(pipelineRecord, jobRecord, "问题数量超过质量门禁设置", data);
                    } else {
                        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                    }
                }
            }
        } else {
            super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
        }

        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());

        // 通知code的扫描结果
        long repoId = Long.parseLong(pipeline.getRepoKey());
        internalScanService.saveSnQbScanRecord(pipeline.getCompanyId(), repoId, pipelineRecord.getExternalName(),
                pipelineRecord.getCommitId(), sonarqubeBuild.getVulnerabilities(), sonarqubeBuild.getBugs(),
                sonarqubeBuild.getCodeSmells(), sonarqubeBuild.getDashboardUrl());
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}