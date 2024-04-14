package com.ezone.devops.plugins.job.test.jacocotest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.test.jacocotest.dao.JacocoTestConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_jacoco_test_config")
public class JacocoTestConfig extends LongID {

    @Column(JacocoTestConfigDao.ID)
    private Long id;

    @Column(JacocoTestConfigDao.ENABLE_QUALITY_CONTROL)
    private boolean enableQualityControl = false;

    // 指令覆盖率
    @Column(JacocoTestConfigDao.INSTRUCTIONS_COVERAGE_GREATER_THAN)
    private Integer instructionsCoverageGreaterThan;
    // 行覆盖率
    @Column(JacocoTestConfigDao.LINE_COVERAGE_GREATER_THAN)
    private Integer lineCoverageGreaterThan;
    // 分支覆盖率
    @Column(JacocoTestConfigDao.BRANCH_COVERAGE_GREATER_THAN)
    private Integer branchCoverageGreaterThan;
    // 方法覆盖率
    @Column(JacocoTestConfigDao.METHOD_COVERAGE_GREATER_THAN)
    private Integer methodCoverageGreaterThan;
    // 类覆盖率
    @Column(JacocoTestConfigDao.CLASS_COVERAGE_GREATER_THAN)
    private Integer classCoverageGreaterThan;

    @Column(JacocoTestConfigDao.CLONE_MODE)
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(JacocoTestConfigDao.BUILD_IMAGE_ID)
    private Long buildImageId;

    @Column(JacocoTestConfigDao.COMMAND)
    private String command;
    @Column(JacocoTestConfigDao.REPORT_DIR)
    private String reportDir;

}