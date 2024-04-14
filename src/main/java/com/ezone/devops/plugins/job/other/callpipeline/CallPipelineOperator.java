package com.ezone.devops.plugins.job.other.callpipeline;

import com.ezone.devops.pipeline.clients.EzPipelineClient;
import com.ezone.devops.pipeline.clients.response.PipelineBuildResponse;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineBuild;
import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineConfig;
import com.ezone.devops.plugins.job.other.callpipeline.service.CallPipelineBuildService;
import com.ezone.ezbase.iam.bean.CompanyIdentityUser;
import com.ezone.ezbase.iam.bean.enums.UserIdentityType;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CallPipelineOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String DASHBOARD_URL = "/ezPipeline/records/detail/%s?pipelineId=%s&repoName=%s";

    @Autowired
    private EzPipelineClient ezPipelineClient;
    @Autowired
    private CallPipelineDataOperator callPipelineDataOperator;
    @Autowired
    private CallPipelineBuildService callPipelineBuildService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        CallPipelineConfig callPipelineConfig = callPipelineDataOperator.getRealJob(jobRecord.getPluginId());
        try {
            Long companyId = repo.getCompanyId();
            RepoVo triggerRepo = repoService.getRepoByNameIfPresent(companyId, callPipelineConfig.getRepoName());
            String triggerUser = jobRecord.getTriggerUser();

            Pipeline triggeredPipeline = pipelineService.getPipeline(triggerRepo, callPipelineConfig.getPipelineName());
            if (triggeredPipeline == null) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "触发的流水线不存在");
                return false;
            }

            if (isNotCompanyAdmin(companyId, triggerUser)) {
                boolean hasExecutePermission = pipelinePermissionService.hasPermission(repo, triggeredPipeline, PipelinePermissionType.PIPELINE_OPERATOR, triggerUser);
                if (!hasExecutePermission) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, I18nContextHolder.get("permission.deny"));
                    return false;
                }
            }


            if (jobRecord.getPipelineId().equals(triggeredPipeline.getId())) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "流水线不允许触发自己");
                return false;
            }

            String callbackUrl = StringUtils.EMPTY;
            if (callPipelineConfig.isNeedCallback()) {
                callbackUrl = getCallbackUrl(jobRecord);
            }

            PipelineBuildResponse pipelineRecordResponse = ezPipelineClient.triggerPipeline(
                    triggeredPipeline.getId(), callPipelineConfig.getBranchName(), triggerUser, TriggerMode.API, callbackUrl);
            if (pipelineRecordResponse == null) {
                log.info("invoke ezpipeline error,response is null job:[{}]", jobRecord);
                invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, ezPipelineClient.getApplicationName());
                return false;
            }

            if (pipelineRecordResponse.isError()) {
                log.info("invoke ezpipeline error response:[{}]", pipelineRecordResponse);
                super.noticeJobFailed(pipelineRecord, jobRecord, pipelineRecordResponse.getMessage());
                return false;
            }

            PipelineRecord newPipelineRecord = pipelineRecordResponse.getData();
            String dashboardUrl = generateDashboardUrl(repo, triggeredPipeline, newPipelineRecord);
            CallPipelineBuild callPipelineBuild = callPipelineDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
            callPipelineBuild.setDashboardUrl(dashboardUrl);
            callPipelineBuildService.updateCallPipelineBuild(callPipelineBuild);
            if (!callPipelineConfig.isNeedCallback()) {
                super.noticeJobSuccess(pipelineRecord, jobRecord, StringUtils.EMPTY);
            }

            return true;
        } catch (Exception exception) {
            super.noticeJobFailed(pipelineRecord, jobRecord, exception.getMessage());
            return false;
        }
    }

    private boolean isNotCompanyAdmin(Long companyId, String username) {
        CompanyIdentityUser companyUser = iamCenterService.queryUserByCompanyIdAndUsername(companyId, username);
        return companyUser.getType() != UserIdentityType.ADMIN;
    }

    private String generateDashboardUrl(RepoVo repo, Pipeline pipeline, PipelineRecord newPipelineRecord) {
        return String.format(DASHBOARD_URL, newPipelineRecord.getId(), pipeline.getId(), repo.getRepoName());
    }

}