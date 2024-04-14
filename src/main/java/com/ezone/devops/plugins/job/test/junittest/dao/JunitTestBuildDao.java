package com.ezone.devops.plugins.job.test.junittest.dao;

import com.ezone.devops.plugins.job.test.junittest.model.JunitTestBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface JunitTestBuildDao extends LongKeyBaseDao<JunitTestBuild> {

    String ID = "id";
    String REPORT_ID = "report_id";
    String TEST_SUCCESS = "test_success";
    String TESTS = "tests";
    String ERRORS = "errors";
    String SKIPPED = "skipped";
    String FAILURES = "failures";
    String SUCCESS_RATE = "success_rate";
    String TIME = "time";
}
