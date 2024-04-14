package com.ezone.devops.plugins.job.other.callpipeline.dao;

import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface CallPipelineBuildDao extends LongKeyBaseDao<CallPipelineBuild> {

    String ID = "id";
    String DASHBOARD_URL = "dashboard_url";
}
