package com.ezone.devops.plugins.job.release;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.ezone.devops.ezcode.sdk.bean.enums.CardRelateType;
import com.ezone.devops.pipeline.clients.request.ArtifactReleasePayload;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.ReleaseVersion;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.service.VersionService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.release.beans.ReleaseArtifact;
import com.ezone.devops.plugins.job.release.model.ArtifactReleaseBuild;
import com.ezone.devops.plugins.job.release.service.ArtifactReleaseBuildService;
import com.ezone.devops.plugins.model.ArtifactInfo;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ArtifactReleaseOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private ArtifactReleaseDataOperator artifactReleaseDataOperator;
    @Autowired
    private ArtifactReleaseBuildService artifactReleaseBuildService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        ArtifactReleaseBuild releaseBuild = artifactReleaseDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        if (null == releaseBuild) {
            super.noticeJobFailed(pipelineRecord, jobRecord, I18nContextHolder.get("job.init.error"));
            return false;
        }

        String version = releaseBuild.getVersion();
        ReleaseVersion releaseVersion = versionService.getReleaseVersion(repo, version);
        // 查询是否已经发不过相同的版本
        if (releaseVersion != null) {
            log.info("release artifact has save version:[{}]", version);
            super.noticeJobFailed(pipelineRecord, jobRecord, "版本号已经存在");
            return false;
        }

        StringBuilder tooltipMessage = new StringBuilder();
        if (releaseBuild.isPushTag()) {
            Set<String> cardKeys = JsonUtils.toObject(releaseBuild.getCardKeys(), new com.fasterxml.jackson.core.type.TypeReference<Set<String>>() {
            });
            CardRelateType relateType = TriggerMode.toCardRelateType(pipelineRecord.getTriggerMode());
            String cardRelateKey = null;
            if (relateType != null) {
                cardRelateKey = getCardRelateKey(pipelineRecord, relateType);
            }

            BaseResponse<?> tagResponse = internalTagService.pipelineReleaseTag(repo.getCompanyId(), repo.getLongRepoKey(),
                    version, pipelineRecord.getCommitId(), jobRecord.getTriggerUser(), releaseBuild.getMessage(),
                    cardRelateKey, relateType, cardKeys, releaseBuild.getSqlScript());
            if (tagResponse == null) {
                log.info("invoke ezcode error,response is null, job:[{}]", jobRecord);
                super.invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, EZCODE_PLATFORM_NAME);
                return false;
            }

            String message = tagResponse.getMessage();
            if (tagResponse.isError()) {
                log.info("invoke ezcode error, response:[{}]", tagResponse);
                super.noticeJobFailed(pipelineRecord, jobRecord, message);
                return false;
            }

            if (StringUtils.isNotEmpty(message)) {
                tooltipMessage.append(message).append("\r");
            }
        }

        List<ArtifactInfo> artifactInfos = artifactInfoService.getByCondition(pipelineRecord.getId(), true);
        if (CollectionUtils.isNotEmpty(artifactInfos)) {
            ArtifactReleasePayload payload = new ArtifactReleasePayload(repo, jobRecord, version, artifactInfos);
            BaseResponse<?> response = ezPackageClient.releaseArtifact(payload);
            if (response == null) {
                log.info("invoke ezpakage error,response is null, job:[{}]", jobRecord);
                super.invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, ezPackageClient.getPlatformName());
                return false;
            }

            if (response.isError()) {
                log.info("invoke ezpakage error, response:[{}]", response);
                super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
                return false;
            }

            jobRecord.setMessage(tooltipMessage.toString());
        } else {
            releaseBuild.setPublish(true);
            artifactReleaseBuildService.updateBuild(releaseBuild);
            versionService.publishReleaseVersion(repo, version, releaseBuild.getMessage());

            pipelineRecord.setReleaseVersion(version);
            pipelineRecordService.updatePipelineRecord(pipelineRecord);

            super.noticeJobSuccess(pipelineRecord, jobRecord, tooltipMessage.toString());
            return true;
        }
        return true;
    }


    private String getCardRelateKey(PipelineRecord pipelineRecord, CardRelateType relateType) {
        return CardRelateType.COMMIT == relateType ? pipelineRecord.getCommitId() : pipelineRecord.getExternalKey();
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONArray data) {
        log.info("receive artifact release success callback:[{}]", jobRecord);
        ArtifactReleaseBuild releaseBuild = artifactReleaseDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        String version = releaseBuild.getVersion();
        if (!releaseBuild.isPublish()) {
            Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
            com.ezone.devops.pipeline.vo.RepoVo repo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
            log.info("start update artifact release version:[{}]", releaseBuild);

            List<ReleaseArtifact> releaseArtifacts = data.toJavaObject(new TypeReference<List<ReleaseArtifact>>() {
            });
            artifactInfoService.updatePublishedResult(pipelineRecord, releaseArtifacts);

            // 通知插件执行成功
            releaseBuild.setPublish(true);
            artifactReleaseBuildService.updateBuild(releaseBuild);
            versionService.publishReleaseVersion(repo, version, releaseBuild.getMessage());

            pipelineRecord.setReleaseVersion(version);
            pipelineRecordService.updatePipelineRecord(pipelineRecord);
            log.info("finished update artifact release version:[{}]", releaseBuild);
        }

        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobFailedCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONArray data) {
        List<ReleaseArtifact> releaseArtifacts = data.toJavaObject(new TypeReference<List<ReleaseArtifact>>() {
        });
        artifactInfoService.updatePublishedResult(pipelineRecord, releaseArtifacts);
        super.jobFailedCallback(pipelineRecord, jobRecord, message, data);
    }
}