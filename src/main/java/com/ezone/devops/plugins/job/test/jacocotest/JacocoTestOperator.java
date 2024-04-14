package com.ezone.devops.plugins.job.test.jacocotest;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.test.jacocotest.enums.JacocoMetricLevel;
import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestBuild;
import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestConfig;
import com.ezone.devops.plugins.job.test.jacocotest.service.JacocoTestBuildService;
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
public class JacocoTestOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String REPORT_INDEX = "index.html";

    @Autowired
    private JacocoTestDataOperator jacocoTestDataOperator;
    @Autowired
    private JacocoTestBuildService jacocoTestBuildService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        JacocoTestConfig jacocoTestConfig = jacocoTestDataOperator.getRealJob(jobRecord.getPluginId());
        JacocoTestBuild jacocoTestBuild = jacocoTestDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());

        String reportId = createReportInfo(pipeline);
        jacocoTestBuild.setReportId(reportId);
        jacocoTestBuildService.updateBuild(jacocoTestBuild);

        String image = getBuildImageUrl(jacocoTestConfig.getBuildImageId());

        ReportUploadStep reportUploadStep = new ReportUploadStep()
                .setReportId(reportId)
                .setUploadReport(true)
                .setReportDir(jacocoTestConfig.getReportDir())
                .setAutoGenerateConfig(true)
                .setUserHomePath(DEFAULT_HOME_PATH)
                .setPublicRepoNames(Sets.newHashSet(DEFAULT_MAVEN_REPO))
                .setPrivateRepoNames(Sets.newHashSet(DEFAULT_MAVEN_REPO));

        CommonJob<ReportUploadStep> commonJob = new CommonJob<ReportUploadStep>()
                .setCloneMode(jacocoTestConfig.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(jacocoTestConfig.getCommand())
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
        JacocoTestConfig jacocoTestConfig = jacocoTestDataOperator.getRealJob(jobRecord.getPluginId());
        JacocoTestBuild jacocoTestBuild = jacocoTestDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());

        String dashboardUrl = getDashBoardUrl(jacocoTestBuild);
        jacocoTestBuild.setDashboardUrl(dashboardUrl);
        jacocoTestBuild.setTestSuccess(true);
        jacocoTestBuildService.updateBuild(jacocoTestBuild);

        boolean resultPassed = true;

        // 打开了指令覆盖率
        Integer instructionsCoverageConfig = jacocoTestConfig.getInstructionsCoverageGreaterThan();
        if (instructionsCoverageConfig != null) {
            Float instructionsCoverage = jacocoTestBuild.getInstructionsCoverage();
            if (instructionsCoverage != null && instructionsCoverageConfig > instructionsCoverage) {
                resultPassed = false;
                jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        JacocoMetricLevel.INSTRUCTIONS_COVERAGE.getType(), instructionsCoverageConfig), data);
            }
        }

        // 打开了行覆盖率
        Integer lineCoverageConfig = jacocoTestConfig.getLineCoverageGreaterThan();
        if (resultPassed && lineCoverageConfig != null) {
            if (jacocoTestBuild.getLineCoverage() != null && lineCoverageConfig > jacocoTestBuild.getLineCoverage()) {
                resultPassed = false;
                jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        JacocoMetricLevel.LINE_COVERAGE.getType(), lineCoverageConfig), data);
            }
        }

        // 打开了分支覆盖率
        Integer branchCoverageConfig = jacocoTestConfig.getBranchCoverageGreaterThan();
        if (resultPassed && branchCoverageConfig != null) {
            Float branchCoverage = jacocoTestBuild.getBranchCoverage();
            if (branchCoverage != null && branchCoverageConfig > branchCoverage) {
                resultPassed = false;
                jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        JacocoMetricLevel.BRANCH_COVERAGE.getType(), branchCoverageConfig), data);
            }
        }

        // 打开了方法覆盖率
        Integer methodCoverageConfig = jacocoTestConfig.getMethodCoverageGreaterThan();
        if (resultPassed && methodCoverageConfig != null) {
            Float methodCoverage = jacocoTestBuild.getMethodCoverage();
            if (methodCoverage != null && methodCoverageConfig > methodCoverage) {
                resultPassed = false;
                jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        JacocoMetricLevel.METHOD_COVERAGE.getType(), methodCoverageConfig), data);
            }
        }

        // 打开了类覆盖率
        Integer classCoverageConfig = jacocoTestConfig.getClassCoverageGreaterThan();
        if (resultPassed && classCoverageConfig != null) {
            Float classCoverage = jacocoTestBuild.getClassCoverage();
            if (classCoverage != null && classCoverageConfig > classCoverage) {
                resultPassed = false;
                jobFailedCallback(pipelineRecord, jobRecord, I18nContextHolder.get("test.result.over.limit",
                        JacocoMetricLevel.CLASS_COVERAGE.getType(), classCoverageConfig), data);
            }
        }

        if (resultPassed) {
            super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
        }
    }

    private String getDashBoardUrl(JacocoTestBuild jacocoTestBuild) {
        return String.format(REPORT_DASH_BOARD_URL, jacocoTestBuild.getReportId(), REPORT_INDEX);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}