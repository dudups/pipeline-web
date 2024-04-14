package com.ezone.devops.plugins.job.test.maventest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.test.maventest.dao.MavenTestConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_maven_test_config")
public class MavenTestConfig extends LongID {

    @Column(MavenTestConfigDao.ID)
    private Long id;
    @Column(MavenTestConfigDao.ENABLE_QUALITY_CONTROL)
    private boolean enableQualityControl = false;
    @Column(MavenTestConfigDao.GREATER_THAN)
    private Integer greaterThan;
    @Column(MavenTestConfigDao.CLONE_MODE)
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(MavenTestConfigDao.BUILD_IMAGE_ID)
    private Long buildImageId;
    @Column(MavenTestConfigDao.COMMAND)
    private String command;
    @Column(MavenTestConfigDao.REPORT_DIR)
    private String reportDir;
    @Column(MavenTestConfigDao.REPORT_INDEX)
    private String reportIndex;

}