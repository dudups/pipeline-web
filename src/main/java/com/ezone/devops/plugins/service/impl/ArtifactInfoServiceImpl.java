package com.ezone.devops.plugins.service.impl;

import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.plugins.dao.ArtifactInfoDao;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.enums.ReleaseStatus;
import com.ezone.devops.plugins.job.release.beans.ReleaseArtifact;
import com.ezone.devops.plugins.model.ArtifactInfo;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArtifactInfoServiceImpl implements ArtifactInfoService {

    @Autowired
    private ArtifactInfoDao artifactInfoDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArtifactInfo saveArtifactInfo(PipelineRecord pipelineRecord, ArtifactType artifactType, String pkgRepo,
                                         String pkgName, String version) {
        ArtifactInfo artifactInfo = new ArtifactInfo();
        artifactInfo.setPipelineBuildId(pipelineRecord.getId());
        artifactInfo.setFormat(artifactType);
        artifactInfo.setPkgRepo(pkgRepo);
        artifactInfo.setPkgName(pkgName);
        artifactInfo.setSnapshotVersion(version);
        artifactInfo.setReleaseVersion(StringUtils.EMPTY);
        artifactInfo.setPublished(false);
        artifactInfo.setReleaseStatus(ReleaseStatus.NONE);
        artifactInfo.setReleaseResult(StringUtils.EMPTY);
        artifactInfoDao.save(artifactInfo);
        return artifactInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateArtifactInfo(PipelineRecord pipelineRecord, Collection<ReleaseArtifact> releaseArtifacts) {
        if (CollectionUtils.isEmpty(releaseArtifacts)) {
            return false;
        }

        Map<Long, ReleaseArtifact> results = releaseArtifacts.stream()
                .collect(Collectors.toMap(ReleaseArtifact::getId, releaseArtifact -> releaseArtifact));
        List<ArtifactInfo> artifactInfos = artifactInfoDao.findByPipelineBuildId(pipelineRecord.getId(), results.keySet());
        if (CollectionUtils.isEmpty(artifactInfos)) {
            return false;
        }

        for (ArtifactInfo artifactInfo : artifactInfos) {
            ReleaseArtifact releaseArtifact = results.get(artifactInfo.getId());
            artifactInfo.setPublished(releaseArtifact.isPublished());
            artifactInfo.setReleaseVersion(releaseArtifact.getReleaseVersion());
        }

        artifactInfoDao.update(artifactInfos);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePublishedResult(PipelineRecord pipelineRecord, Collection<ReleaseArtifact> releaseArtifacts) {
        if (CollectionUtils.isEmpty(releaseArtifacts)) {
            return false;
        }

        Map<Long, ReleaseArtifact> results = releaseArtifacts.stream()
                .collect(Collectors.toMap(ReleaseArtifact::getId, releaseArtifact -> releaseArtifact));
        List<ArtifactInfo> artifactInfos = artifactInfoDao.findByPipelineBuildId(pipelineRecord.getId(), results.keySet());
        if (CollectionUtils.isEmpty(artifactInfos)) {
            return false;
        }

        for (ArtifactInfo artifactInfo : artifactInfos) {
            ReleaseArtifact releaseArtifact = results.get(artifactInfo.getId());
            artifactInfo.setReleaseStatus(releaseArtifact.getReleaseStatus());
            artifactInfo.setReleaseResult(releaseArtifact.getReleaseResult());
        }

        artifactInfoDao.update(artifactInfos);
        return true;
    }

    @Override
    public List<ArtifactInfo> getAllByPipelineRecord(PipelineRecord pipelineRecord) {
        return artifactInfoDao.findByPipelineBuildId(pipelineRecord.getId());
    }

    @Override
    public List<ArtifactInfo> getByCondition(Long pipelineBuildId, boolean published) {
        return artifactInfoDao.findByPipelineBuildIdAndPublished(pipelineBuildId, published);
    }
}
