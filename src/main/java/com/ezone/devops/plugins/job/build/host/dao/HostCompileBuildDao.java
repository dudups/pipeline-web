package com.ezone.devops.plugins.job.build.host.dao;

import com.ezone.devops.plugins.job.build.host.model.HostCompileBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HostCompileBuildDao extends LongKeyBaseDao<HostCompileBuild> {

    String ID = "id";
    String REPORT_DASHBOARD_URL = "report_dashboard_url";
    String REPORT_ID = "report_id";
}
