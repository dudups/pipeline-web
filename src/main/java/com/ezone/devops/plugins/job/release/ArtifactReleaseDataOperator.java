package com.ezone.devops.plugins.job.release;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.VersionAlreadyExistException;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyConfig;
import com.ezone.devops.plugins.job.release.beans.ReleaseArtifact;
import com.ezone.devops.plugins.job.release.beans.ReleaseArtifactInfo;
import com.ezone.devops.plugins.job.release.model.ArtifactReleaseBuild;
import com.ezone.devops.plugins.job.release.service.ArtifactReleaseBuildService;
import com.ezone.devops.plugins.model.ArtifactInfo;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArtifactReleaseDataOperator implements PluginDataOperator<EmptyConfig, ArtifactReleaseBuild> {

    private static final int MAX_MESSAGE_LENGTH = 4000;
    private static final int MAX_SQL_LENGTH = 4000;

    @Autowired
    private ArtifactReleaseBuildService artifactReleaseBuildService;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        ArtifactReleaseBuild artifactReleaseBuild = json.toJavaObject(ArtifactReleaseBuild.class);
        return artifactReleaseBuildService.updateBuild(artifactReleaseBuild);
    }

    @Override
    public boolean updateRealJobBuildWithFields(Long realJobRecordId, JSONObject updateFields) {
        if (null == updateFields) {
            return false;
        }

        ArtifactReleaseBuild artifactReleaseBuild = getRealJobRecord(realJobRecordId);
        PipelineRecord pipelineRecord = pipelineRecordService.getByIdIfPresent(artifactReleaseBuild.getPipelineBuildId());

        boolean releasedVersion = artifactReleaseBuildService.hasSameReleasedVersion(artifactReleaseBuild);
        if (releasedVersion) {
            throw new VersionAlreadyExistException();
        }

        if (StringUtils.length(artifactReleaseBuild.getMessage()) > MAX_MESSAGE_LENGTH) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "发版版本的信息太长，最多支持1000字");
        }

        if (StringUtils.length(artifactReleaseBuild.getSqlScript()) > MAX_SQL_LENGTH) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "sql脚本最长不的超过4000字");
        }


        ReleaseArtifactInfo releaseArtifactInfo = updateFields.toJavaObject(ReleaseArtifactInfo.class);
        if (null == releaseArtifactInfo) {
            return false;
        }

        if (StringUtils.isBlank(releaseArtifactInfo.getReleaseVersion())) {
            return false;
        }

        artifactReleaseBuild.setPushTag(releaseArtifactInfo.isPushTag());
        artifactReleaseBuild.setMessage(StringUtils.defaultIfBlank(releaseArtifactInfo.getMessage(), StringUtils.EMPTY));
        artifactReleaseBuild.setSqlScript(StringUtils.defaultIfBlank(releaseArtifactInfo.getSqlScript(), StringUtils.EMPTY));
        artifactReleaseBuild.setVersion(releaseArtifactInfo.getReleaseVersion());
        artifactReleaseBuild.setCardKeys(JsonUtils.toJson(releaseArtifactInfo.getCardsKeys()));
        artifactReleaseBuildService.updateBuild(artifactReleaseBuild);

        List<ArtifactInfo> artifactInfos = artifactInfoService.getAllByPipelineRecord(pipelineRecord);
        if (CollectionUtils.isNotEmpty(artifactInfos)) {
            List<ReleaseArtifact> releaseArtifacts = releaseArtifactInfo.getReleaseArtifacts();
            // 更新发版制品的设置
            artifactInfoService.updateArtifactInfo(pipelineRecord, releaseArtifacts);
        }

        return true;
    }

    @Override
    public ArtifactReleaseBuild getRealJobRecord(Long id) {
        return artifactReleaseBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        PipelineRecord pipelineRecord = context.getPipelineRecord();
        ArtifactReleaseBuild artifactReleaseBuild = new ArtifactReleaseBuild(pipelineRecord);
        artifactReleaseBuildService.saveBuild(artifactReleaseBuild);
        log.debug("save build:[{}]", artifactReleaseBuild);
        return artifactReleaseBuild.getId();
    }
}
