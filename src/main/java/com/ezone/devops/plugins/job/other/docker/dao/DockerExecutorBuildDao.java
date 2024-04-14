package com.ezone.devops.plugins.job.other.docker.dao;

import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface DockerExecutorBuildDao extends LongKeyBaseDao<DockerExecutorBuild> {

    String ID = "id";
    String REPORT_DASHBOARD_URL = "report_dashboard_url";
    String REPORT_ID = "report_id";
    String VERSION = "version";
}
