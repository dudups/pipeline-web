package com.ezone.devops.plugins.service;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.vo.RepoVo;

public interface ItsmCheckService {
    String isPass(RepoVo repo, String clusterKey, String nameSpace, JobRecord jobRecord);
}
