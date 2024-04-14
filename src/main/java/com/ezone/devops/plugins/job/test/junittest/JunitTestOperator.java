package com.ezone.devops.plugins.job.test.junittest;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.test.junittest.enums.JunitMetricLevel;
import com.ezone.devops.plugins.job.test.junittest.model.JunitTestBuild;
import com.ezone.devops.plugins.job.test.junittest.model.JunitTestConfig;
import com.ezone.devops.plugins.job.test.junittest.service.JunitTestBuildService;
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
public class JunitTestOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private JunitTestDataOperator junitTestDataOperator;
    @Autowired
    private JunitTestBuildService junitTestBuildService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        JunitTestConfig junitTestConfig = junitTestDataOperator.getRealJob(jobRecord.getPluginId());
        JunitTestBuild junitTestBuild = junitTestDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        if (null == junitTestBuild) {
            super.noticeJobFailed(pipelineRecord, jobRecord, I18nContextHolder.get("job.init.error"));
            return false;
        }

        String reportId = createReportInfo(pipeline);
        junitTestBuild.setReportId(reportId);
        junitTestBuildService.updateBuild(junitTestBuild);

        String image = getBuildImageUrl(junitTestConfig.getBuildImageId());

        ReportUploadStep reportUploadStep = new ReportUploadStep()
                .setReportId(reportId)
                .setUploadReport(false)
                .setReportDir(junitTestConfig.getReportDir())
                .setAutoGenerateConfig(true)
                .setUserHomePath(DEFAULT_HOME_PATH)
                .setPublicRepoNames(Sets.newHashSet(DEFAULT_MAVEN_REPO))
                .setPrivateRepoNames(Sets.newHashSet(DEFAULT_MAVEN_REPO));

        CommonJob<ReportUploadStep> commonJob = new CommonJob<ReportUploadStep>()
                .setCloneMode(junitTestConfig.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(junitTestConfig.getCommand())
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
        JunitTestConfig junitTestConfig = junitTestDataOperator.getRealJob(jobRecord.getPluginId());
        JunitTestBuild junitTestBuild = junitTestDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        junitTestBuild.setTestSuccess(true);
        junitTestBuildService.updateBuild(junitTestBuild);

        if (junitTestConfig.isEnableQualityControl()) {
            Integer successConfig = junitTestConfig.getGreaterThan();
            Float successRate = junitTestBuild.getSuccessRate();
            if (successRate != null && successConfig > successRate) {
                super.jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        JunitMetricLevel.PASS_RATE.getType(), successConfig), data);
            } else {
                super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
            }
        } else {
            super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
        }
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }

}