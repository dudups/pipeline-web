package com.ezone.devops.plugins.job.other.callpipeline.dao;

import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface CallPipelineConfigDao extends LongKeyBaseDao<CallPipelineConfig> {

    String ID = "id";
    String REPO_NAME = "repo_name";
    String PIPELINE_NAME = "pipeline_name";
    String BRANCH_NAME = "branch_name";
    String NEED_CALLBACK = "need_callback";
}
