package com.ezone.devops.plugins.job.test.junittest.dao;

import com.ezone.devops.plugins.job.test.junittest.model.JunitTestConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface JunitTestConfigDao extends LongKeyBaseDao<JunitTestConfig> {

    String ID = "id";
    String ENABLE_QUALITY_CONTROL = "enable_quality_control";
    String GREATER_THAN = "greater_than";
    String CLONE_MODE = "clone_mode";
    String BUILD_IMAGE_ID = "build_image_id";
    String COMMAND = "command";
    String REPORT_DIR = "report_dir";
}
