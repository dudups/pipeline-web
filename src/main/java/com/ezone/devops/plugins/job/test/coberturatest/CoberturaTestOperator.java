package com.ezone.devops.plugins.job.test.coberturatest;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.test.coberturatest.enums.CoberturaMetricLevel;
import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestBuild;
import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestConfig;
import com.ezone.devops.plugins.job.test.coberturatest.service.CoberturaTestBuildService;
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
public class CoberturaTestOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String REPORT_INDEX = "index.html";

    @Autowired
    private CoberturaTestDataOperator coberturaTestDataOperator;
    @Autowired
    private CoberturaTestBuildService coberturaTestBuildService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        CoberturaTestConfig coberturaTestConfig = coberturaTestDataOperator.getRealJob(jobRecord.getPluginId());
        CoberturaTestBuild coberturaTestBuild = coberturaTestDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        if (null == coberturaTestBuild) {
            super.noticeJobFailed(pipelineRecord, jobRecord, I18nContextHolder.get("job.init.error"));
            return false;
        }

        String reportId = createReportInfo(pipeline);
        coberturaTestBuild.setReportId(reportId);
        coberturaTestBuildService.updateBuild(coberturaTestBuild);

        String image = getBuildImageUrl(coberturaTestConfig.getBuildImageId());

        ReportUploadStep reportUploadStep = new ReportUploadStep()
                .setReportId(reportId)
                .setUploadReport(true)
                .setReportDir(coberturaTestConfig.getReportDir())
                .setAutoGenerateConfig(true)
                .setUserHomePath(DEFAULT_HOME_PATH)
                .setPublicRepoNames(Sets.newHashSet(DEFAULT_MAVEN_REPO))
                .setPrivateRepoNames(Sets.newHashSet(DEFAULT_MAVEN_REPO));

        CommonJob<ReportUploadStep> commonJob = new CommonJob<ReportUploadStep>()
                .setCloneMode(coberturaTestConfig.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(coberturaTestConfig.getCommand())
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
        CoberturaTestConfig coberturaTestConfig = coberturaTestDataOperator.getRealJob(jobRecord.getPluginId());
        CoberturaTestBuild coberturaTestBuild = coberturaTestDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());

        String dashboardUrl = getDashBoardUrl(coberturaTestBuild);
        coberturaTestBuild.setDashboardUrl(dashboardUrl);
        coberturaTestBuild.setTestSuccess(true);
        coberturaTestBuildService.updateBuild(coberturaTestBuild);

        boolean resultPassed = true;
        // 打开了行覆盖率
        Integer lineCoverageConfig = coberturaTestConfig.getLineCoverageGreaterThan();
        if (lineCoverageConfig != null) {
            if (coberturaTestBuild.getLineCoverage() != null
                    && lineCoverageConfig > coberturaTestBuild.getLineCoverage()) {
                resultPassed = false;
                super.jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        CoberturaMetricLevel.LINE_COVERAGE.getType(), lineCoverageConfig), data);
            }
        }

        // 打开了分支覆盖率
        Integer branchCoverageConfig = coberturaTestConfig.getBranchCoverageGreaterThan();
        if (resultPassed && branchCoverageConfig != null) {
            if (coberturaTestBuild.getBranchCoverage() != null
                    && branchCoverageConfig > coberturaTestBuild.getBranchCoverage()) {
                resultPassed = false;
                super.jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        CoberturaMetricLevel.LINE_COVERAGE.getType(), branchCoverageConfig), data);
            }
        }

        if (resultPassed) {
            super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
        }
    }


    private String getDashBoardUrl(CoberturaTestBuild coberturaTestBuild) {
        return String.format(REPORT_DASH_BOARD_URL, coberturaTestBuild.getReportId(), REPORT_INDEX);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}