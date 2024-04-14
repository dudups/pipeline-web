package com.ezone.devops.plugins.job.test.jacocotest.dao;

import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface JacocoTestBuildDao extends LongKeyBaseDao<JacocoTestBuild> {

    String ID = "id";
    String REPORT_ID = "report_id";
    String TEST_SUCCESS = "test_success";
    String DASHBOARD_URL = "dashboard_url";
    String INSTRUCTIONS_COVERAGE = "instructions_coverage";
    String BRANCH_COVERAGE = "branch_coverage";
    String LINE_COVERAGE = "line_coverage";
    String METHOD_COVERAGE = "method_coverage";
    String CLASS_COVERAGE = "class_coverage";
}
