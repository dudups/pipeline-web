package com.ezone.devops.plugins.job.scan.sonarqube.dao;

import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface SonarqubeConfigDao extends LongKeyBaseDao<SonarqubeConfig> {

    String ID = "id";
    String PROVIDER_NAME = "provider_name";
    String BUILD_IMAGE_ID = "build_image_id";
    String SONAR_PROJECT_CONTENT = "sonar_project_content";
    String OVERRIDE_SONAR_CONFIG = "override_sonar_config";
    String ENABLE_QUALITY_CONTROL = "enable_quality_control";
    String METRIC_LEVEL = "metric_level";
    String GREATER_THAN = "greater_than";
}
