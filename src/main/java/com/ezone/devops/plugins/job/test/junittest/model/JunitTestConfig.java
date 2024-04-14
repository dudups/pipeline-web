package com.ezone.devops.plugins.job.test.junittest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.test.junittest.dao.JunitTestConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_junit_test_config")
public class JunitTestConfig extends LongID {

    @Column(JunitTestConfigDao.ID)
    private Long id;
    @Column(JunitTestConfigDao.ENABLE_QUALITY_CONTROL)
    private boolean enableQualityControl = false;
    @Column(JunitTestConfigDao.GREATER_THAN)
    private Integer greaterThan;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(JunitTestConfigDao.BUILD_IMAGE_ID)
    private Long buildImageId;
    @Column(JunitTestConfigDao.CLONE_MODE)
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    @Column(JunitTestConfigDao.COMMAND)
    private String command;
    @Column(JunitTestConfigDao.REPORT_DIR)
    private String reportDir;

}