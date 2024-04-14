package com.ezone.devops.plugins.job.test.jacocotest.dao;

import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface JacocoTestConfigDao extends LongKeyBaseDao<JacocoTestConfig> {

    String ID = "id";
    String ENABLE_QUALITY_CONTROL = "enable_quality_control";
    String INSTRUCTIONS_COVERAGE_GREATER_THAN = "instructions_coverage_greater_than";
    String LINE_COVERAGE_GREATER_THAN = "line_coverage_greater_than";
    String BRANCH_COVERAGE_GREATER_THAN = "branch_coverage_greater_than";
    String METHOD_COVERAGE_GREATER_THAN = "method_coverage_greater_than";
    String CLASS_COVERAGE_GREATER_THAN = "class_coverage_greater_than";
    String CLONE_MODE = "clone_mode";
    String BUILD_IMAGE_ID = "build_image_id";
    String COMMAND = "command";
    String REPORT_DIR = "report_dir";
}
