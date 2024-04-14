package com.ezone.devops.plugins.job.test.coberturatest.dao;

import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface CoberturaTestBuildDao extends LongKeyBaseDao<CoberturaTestBuild> {

    String ID = "id";
    String REPORT_ID = "report_id";
    String TEST_SUCCESS = "test_success";
    String DASHBOARD_URL = "dashboard_url";
    String LINE_COVERAGE = "line_coverage";
    String BRANCH_COVERAGE = "branch_coverage";
}
