package com.ezone.devops.plugins.job.test.maventest;


import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.test.maventest.enums.MavenMetricLevel;
import com.ezone.devops.plugins.job.test.maventest.model.MavenTestBuild;
import com.ezone.devops.plugins.job.test.maventest.model.MavenTestConfig;
import com.ezone.devops.plugins.job.test.maventest.service.MavenTestBuildService;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.ReportUploadStep;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MavenTestOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private MavenTestDataOperator mavenTestDataOperator;
    @Autowired
    private MavenTestBuildService mavenTestBuildService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        MavenTestConfig mavenTestConfig = mavenTestDataOperator.getRealJob(jobRecord.getPluginId());
        MavenTestBuild mavenTestBuild = mavenTestDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        if (null == mavenTestBuild) {
            super.noticeJobFailed(pipelineRecord, jobRecord, I18nContextHolder.get("job.init.error"));
            return false;
        }

        String reportId = createReportInfo(pipeline);
        mavenTestBuild.setReportId(reportId);
        mavenTestBuildService.updateBuild(mavenTestBuild);

        String image = getBuildImageUrl(mavenTestConfig.getBuildImageId());

        ReportUploadStep reportUploadStep = new ReportUploadStep()
                .setReportId(reportId)
                .setUploadReport(true)
                .setReportDir(mavenTestConfig.getReportDir())
                .setReportIndex(mavenTestConfig.getReportIndex())
                .setAutoGenerateConfig(true)
                .setUserHomePath(DEFAULT_HOME_PATH)
                .setPublicRepoNames(Sets.newHashSet(DEFAULT_MAVEN_REPO))
                .setPrivateRepoNames(Sets.newHashSet(DEFAULT_MAVEN_REPO));

        CommonJob<ReportUploadStep> commonJob = new CommonJob<ReportUploadStep>()
                .setCloneMode(mavenTestConfig.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(mavenTestConfig.getCommand())
                .setTempDirs(Sets.newHashSet(generateMavenDefaultSetting()))
                .setSteps(reportUploadStep);

        ImageInfo buildImageInfo = new ImageInfo()
                .setAlias(ImageInfo.BUILD_NAME)
                .setName(image)
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
        MavenTestConfig mavenTestConfig = mavenTestDataOperator.getRealJob(jobRecord.getPluginId());
        MavenTestBuild mavenTestBuild = mavenTestDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());

        String dashboardUrl = getDashBoardUrl(mavenTestConfig, mavenTestBuild);
        mavenTestBuild.setDashboardUrl(dashboardUrl);
        mavenTestBuild.setTestSuccess(true);
        mavenTestBuildService.updateBuild(mavenTestBuild);

        if (mavenTestConfig.isEnableQualityControl()) {
            log.info("maven test open quality control, jobRecord:[{}]", jobRecord.getId());
            Integer successRateConfig = mavenTestConfig.getGreaterThan();
            Float successRate = mavenTestBuild.getSuccessRate();
            if (successRate != null && successRateConfig > successRate) {
                super.jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        MavenMetricLevel.SUCCESS_RATE.getType(), successRateConfig), data);
            } else {
                super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
            }
        } else {
            log.info("maven test not open quality control, jobRecord:[{}]", jobRecord.getId());
            super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
        }
    }

    private String getDashBoardUrl(MavenTestConfig mavenTestConfig, MavenTestBuild mavenTestBuild) {
        return String.format(REPORT_DASH_BOARD_URL, mavenTestBuild.getReportId(), mavenTestConfig.getReportIndex());
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}