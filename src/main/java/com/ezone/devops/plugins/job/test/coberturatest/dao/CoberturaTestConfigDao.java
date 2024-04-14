package com.ezone.devops.plugins.job.test.coberturatest.dao;

import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface CoberturaTestConfigDao extends LongKeyBaseDao<CoberturaTestConfig> {

    String ID = "id";
    String ENABLE_QUALITY_CONTROL = "enable_quality_control";
    String LINE_COVERAGE_GREATER_THAN = "line_coverage_greater_than";
    String BRANCH_COVERAGE_GREATER_THAN = "branch_coverage_greater_than";
    String CLONE_MODE = "clone_mode";
    String BUILD_IMAGE_ID = "build_image_id";
    String COMMAND = "command";
    String REPORT_DIR = "report_dir";
}
