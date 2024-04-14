package com.ezone.devops.plugins.job.test.maventest.dao;

import com.ezone.devops.plugins.job.test.maventest.model.MavenTestBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface MavenTestBuildDao extends LongKeyBaseDao<MavenTestBuild> {

    String ID = "id";
    String REPORT_ID = "report_id";
    String TEST_SUCCESS = "test_success";
    String DASHBOARD_URL = "dashboard_url";
    String TESTS = "tests";
    String ERRORS = "errors";
    String FAILURES = "failures";
    String SUCCESS_RATE = "success_rate";
    String TIME = "time";
}
