package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.SnapshotVersionDao;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "snapshot_version")
public class SnapshotVersion extends LongID {

    public static final Long DEFAULT_VERSION = 0L;

    @Column(SnapshotVersionDao.ID)
    private Long id;
    @Column(SnapshotVersionDao.REPO_KEY)
    private String repoKey;
    @Column(SnapshotVersionDao.VERSION)
    private Long version = DEFAULT_VERSION;

    public SnapshotVersion(RepoVo repo) {
        setRepoKey(repo.getRepoKey());
        setVersion(DEFAULT_VERSION);
    }
}
