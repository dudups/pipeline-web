package com.ezone.devops.pipeline.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.pipeline.dao.ReleaseVersionDao;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "release_version")
public class ReleaseVersion extends LongID {

    public static final String DEFAULT_VERSION = "0.0.0";

    @JSONField(serialize = false)
    @Column(ReleaseVersionDao.ID)
    private Long id;
    @JSONField(serialize = false)
    @Column(ReleaseVersionDao.REPO_KEY)
    private String repoKey;
    @Column(ReleaseVersionDao.VERSION)
    private String version;
    @Column(ReleaseVersionDao.MESSAGE)
    private String message;

    public ReleaseVersion(RepoVo repo) {
        setRepoKey(repo.getRepoKey());
        setVersion(DEFAULT_VERSION);
        setMessage(StringUtils.EMPTY);
    }
}
