package com.ezone.devops.plugins.job.other.callpipeline.model;

import com.ezone.devops.plugins.job.other.callpipeline.dao.CallPipelineConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_call_pipeline_config")
public class CallPipelineConfig extends LongID {

    @Column(CallPipelineConfigDao.ID)
    private Long id;
    @Column(CallPipelineConfigDao.REPO_NAME)
    private String repoName;
    @Column(CallPipelineConfigDao.PIPELINE_NAME)
    private String pipelineName;
    @Column(CallPipelineConfigDao.BRANCH_NAME)
    private String branchName;
    @Column(CallPipelineConfigDao.NEED_CALLBACK)
    private boolean needCallback;
}