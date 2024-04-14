package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.PipelinePermissionDao;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.enums.UserType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "pipeline_permission")
public class PipelinePermission extends LongID {

    @Column(PipelinePermissionDao.ID)
    private Long id;
    @Column(PipelinePermissionDao.COMPANY_ID)
    private Long companyId;
    @Column(PipelinePermissionDao.REPO_KEY)
    private String repoKey;
    @Column(PipelinePermissionDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(PipelinePermissionDao.PERMISSION_TYPE)
    private PipelinePermissionType pipelinePermissionType;
    @Column(PipelinePermissionDao.NAME)
    private String name;
    @Column(PipelinePermissionDao.TYPE)
    private UserType type;

}
