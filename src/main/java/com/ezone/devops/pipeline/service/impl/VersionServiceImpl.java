package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.ReleaseVersionDao;
import com.ezone.devops.pipeline.dao.SnapshotVersionDao;
import com.ezone.devops.pipeline.exception.VersionAlreadyExistException;
import com.ezone.devops.pipeline.lock.DistributedLock;
import com.ezone.devops.pipeline.model.ReleaseVersion;
import com.ezone.devops.pipeline.model.SnapshotVersion;
import com.ezone.devops.pipeline.service.VersionService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.framework.redis.annotation.KLock;
import com.ezone.galaxy.framework.redis.enums.LockTimeoutStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class VersionServiceImpl implements VersionService {

    private static final String SNAPSHOT_VERSION_LOCK_KEY = "repoKey:%s:snapshotVersion:%s";
    private static final long SNAPSHOT_VERSION_LOCK_EXPIRE_DEFAULT = 60000;
    private static final int SNAPSHOT_VERSION_LOCK_ATTEMPT_DEFAULT = 10000;
    @Autowired
    private ReleaseVersionDao releaseVersionDao;
    @Autowired
    private SnapshotVersionDao snapshotVersionDao;
    @Autowired
    private DistributedLock distributedLock;

    @KLock(name = "init_version", keys = "#repo.repoKey", leaseTime = 5000, lockTimeoutStrategy = LockTimeoutStrategy.KEEP_ACQUIRE)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String initVersion(RepoVo repo) {
        log.info("start init repo release version:[{}]", repo);
        ReleaseVersion releaseVersion = releaseVersionDao.getLastVersionByRepoKey(repo.getRepoKey());
        if (releaseVersion == null) {
            releaseVersion = new ReleaseVersion(repo);
            releaseVersion = releaseVersionDao.save(releaseVersion);
        }

        SnapshotVersion snapshotVersion = snapshotVersionDao.getSnapshotVersionByRepoKey(repo.getRepoKey());
        if (snapshotVersion == null) {
            snapshotVersion = new SnapshotVersion(repo);
            snapshotVersion = snapshotVersionDao.save(snapshotVersion);
        }

        log.info("finished init repo release version:[{}]", releaseVersion);
        return releaseVersion.getVersion() + "-" + snapshotVersion.getVersion();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String getNextSnapshotVersion(RepoVo repo) {
        String repoKey = repo.getRepoKey();
        ReleaseVersion releaseVersion = releaseVersionDao.getLastVersionByRepoKey(repoKey);
        if (releaseVersion == null) {
            releaseVersion = new ReleaseVersion(repo);
            releaseVersion = releaseVersionDao.save(releaseVersion);
        }

        SnapshotVersion snapshotVersion = snapshotVersionDao.getSnapshotVersionByRepoKey(repoKey);
        if (snapshotVersion == null) {
            snapshotVersion = new SnapshotVersion(repo);
            snapshotVersionDao.save(snapshotVersion);
        }

        long version = snapshotVersion.getVersion() + 1;
        try {
            long lockAttemptMills = SNAPSHOT_VERSION_LOCK_ATTEMPT_DEFAULT;
            while (lockAttemptMills >= 0) {
                String lockKey = String.format(SNAPSHOT_VERSION_LOCK_KEY, repo.getRepoKey(), version);
                UUID lock = distributedLock.tryLock(lockKey, SNAPSHOT_VERSION_LOCK_EXPIRE_DEFAULT);
                if (lock != null) {
                    break;
                }
                version++;
                lockAttemptMills -= DistributedLock.DEFAULT_ACQUIRE_RESOLUTION_MILLIS;
                Thread.sleep(DistributedLock.DEFAULT_ACQUIRE_RESOLUTION_MILLIS);
            }
        } catch (Exception e) {
            log.warn("repo:{} snapshot version:{} error", repo.getRepoKey(), version, e);
        }

        snapshotVersion.setVersion(version);
        snapshotVersionDao.update(snapshotVersion);
        return releaseVersion.getVersion() + "-" + snapshotVersion.getVersion();
    }

    @Override
    public ReleaseVersion getLastReleaseVersion(RepoVo repo) {
        return releaseVersionDao.getLastVersionByRepoKey(repo.getRepoKey());
    }

    @Override
    public ReleaseVersion getReleaseVersion(RepoVo repo, String version) {
        return releaseVersionDao.getByRepoIdAndVersion(repo.getRepoKey(), version);
    }

    @KLock(name = "publish_version", keys = "#repo.repoKey", leaseTime = 5, lockTimeoutStrategy = LockTimeoutStrategy.KEEP_ACQUIRE)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean publishReleaseVersion(RepoVo repo, String version, String message) {
        String repoKey = repo.getRepoKey();
        ReleaseVersion releaseVersion = releaseVersionDao.getByRepoIdAndVersion(repoKey, version);
        if (releaseVersion != null) {
            throw new VersionAlreadyExistException();
        }

        releaseVersion = new ReleaseVersion();
        releaseVersion.setRepoKey(repoKey);
        releaseVersion.setVersion(version);
        releaseVersion.setMessage(message);
        releaseVersionDao.save(releaseVersion);

        // 重置临时版本
        SnapshotVersion snapshotVersion = snapshotVersionDao.getSnapshotVersionByRepoKey(repoKey);
        snapshotVersion.setVersion(SnapshotVersion.DEFAULT_VERSION);
        snapshotVersionDao.update(snapshotVersion);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByRepo(RepoVo repo) {
        snapshotVersionDao.deleteByRepoKey(repo.getRepoKey());
        releaseVersionDao.deleteByRepoKey(repo.getRepoKey());
    }
}
