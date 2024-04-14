package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.PipelineDao;
import com.ezone.devops.pipeline.enums.ResourceType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.PipelinePayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "pipeline")
public class Pipeline extends LongID {

    @Column(PipelineDao.ID)
    private Long id;
    @Column(PipelineDao.COMPANY_ID)
    private Long companyId;
    @Column(PipelineDao.REPO_KEY)
    private String repoKey;
    @Column(PipelineDao.NAME)
    private String name;
    @Column(PipelineDao.JOB_TIMEOUT_MINUTE)
    private int jobTimeoutMinute;
    @Column(PipelineDao.MATCH_ALL_BRANCH)
    private boolean matchAllBranch;

    @Column(value = PipelineDao.RESOURCE_TYPE)
    private ResourceType resourceType;
    @Column(PipelineDao.CLUSTER_NAME)
    private String clusterName;
    @Column(value = PipelineDao.USE_DEFAULT_CLUSTER)
    private boolean useDefaultCluster;

    @Column(PipelineDao.CREATE_USER)
    private String createUser;
    @Column(value = PipelineDao.CREATE_TIME, maybeModified = false)
    private Date createTime;
    @Column(PipelineDao.MODIFY_TIME)
    private Date modifyTime;

    @Column(PipelineDao.INHERIT_REPO_PERMISSION)
    private boolean inheritRepoPermission;

    public Pipeline(PipelinePayload pipelinePayload, RepoVo repo, String username) {
        setCompanyId(repo.getCompanyId());
        setRepoKey(repo.getRepoKey());

        setName(pipelinePayload.getName());
        setJobTimeoutMinute(pipelinePayload.getJobTimeoutMinute());
        setMatchAllBranch(pipelinePayload.isMatchAllBranch());

        setUseDefaultCluster(pipelinePayload.isUseDefaultCluster());
        setResourceType(pipelinePayload.getResourceType());
        setClusterName(pipelinePayload.getClusterName());

        setCreateUser(username);
        Date createTime = new Date();
        setCreateTime(createTime);
        setModifyTime(createTime);
        setInheritRepoPermission(true);
    }
}
