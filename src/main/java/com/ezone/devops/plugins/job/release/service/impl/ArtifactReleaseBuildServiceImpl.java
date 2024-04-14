package com.ezone.devops.plugins.job.release.service.impl;

import com.ezone.devops.plugins.job.release.dao.ArtifactReleaseBuildDao;
import com.ezone.devops.plugins.job.release.model.ArtifactReleaseBuild;
import com.ezone.devops.plugins.job.release.service.ArtifactReleaseBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArtifactReleaseBuildServiceImpl implements ArtifactReleaseBuildService {

    @Autowired
    private ArtifactReleaseBuildDao artifactReleaseBuildDao;

    @Override
    public ArtifactReleaseBuild getById(Long id) {
        return artifactReleaseBuildDao.get(id);
    }

    @Override
    public ArtifactReleaseBuild getByPipelineBuildId(Long pipelineBuildId) {
        return artifactReleaseBuildDao.findByPipelineBuildId(pipelineBuildId);
    }

    @Override
    public boolean hasSameReleasedVersion(ArtifactReleaseBuild artifactReleaseBuild) {
        ArtifactReleaseBuild releasedVersion = artifactReleaseBuildDao.hasSameReleasedVersion(
                artifactReleaseBuild.getRepoKey(), artifactReleaseBuild.getVersion());
        if (releasedVersion == null) {
            return false;
        }
        return !artifactReleaseBuild.getPipelineBuildId().equals(releasedVersion.getPipelineBuildId());
    }

    @Override
    public ArtifactReleaseBuild saveBuild(ArtifactReleaseBuild artifactReleaseBuild) {
        return artifactReleaseBuildDao.save(artifactReleaseBuild);
    }

    @Override
    public boolean updateBuild(ArtifactReleaseBuild artifactReleaseBuild) {
        return artifactReleaseBuildDao.update(artifactReleaseBuild);
    }

}
