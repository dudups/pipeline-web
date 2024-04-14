package com.ezone.devops.plugins.job.scan.sonarqube.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.job.scan.sonarqube.dao.SonarqubeConfigDao;
import com.ezone.devops.plugins.job.scan.sonarqube.enums.MetricLevel;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_sonarqube_config")
public class SonarqubeConfig extends LongID {

    @Column(SonarqubeConfigDao.ID)
    private Long id;
    @Column(SonarqubeConfigDao.PROVIDER_NAME)
    private String providerName;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(SonarqubeConfigDao.BUILD_IMAGE_ID)
    private Long buildImageId;
    @Column(SonarqubeConfigDao.OVERRIDE_SONAR_CONFIG)
    private boolean overrideSonarConfig = false;
    @Column(SonarqubeConfigDao.SONAR_PROJECT_CONTENT)
    private String sonarProjectContent;

    @Column(SonarqubeConfigDao.ENABLE_QUALITY_CONTROL)
    private boolean enableQualityControl = false;
    @Column(SonarqubeConfigDao.METRIC_LEVEL)
    private MetricLevel metricLevel;
    @Column(SonarqubeConfigDao.GREATER_THAN)
    private Integer greaterThan = 0;
}