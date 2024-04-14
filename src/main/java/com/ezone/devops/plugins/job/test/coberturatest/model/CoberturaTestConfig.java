package com.ezone.devops.plugins.job.test.coberturatest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.test.coberturatest.dao.CoberturaTestConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_cobertura_test_config")
public class CoberturaTestConfig extends LongID {

    @Column(CoberturaTestConfigDao.ID)
    private Long id;

    @Column(CoberturaTestConfigDao.ENABLE_QUALITY_CONTROL)
    private boolean enableQualityControl = false;
    // 行覆盖率开关
    @Column(CoberturaTestConfigDao.LINE_COVERAGE_GREATER_THAN)
    private Integer lineCoverageGreaterThan;
    // 分支覆盖率开关
    @Column(CoberturaTestConfigDao.BRANCH_COVERAGE_GREATER_THAN)
    private Integer branchCoverageGreaterThan;

    @Column(CoberturaTestConfigDao.CLONE_MODE)
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(CoberturaTestConfigDao.BUILD_IMAGE_ID)
    private Long buildImageId;
    @Column(CoberturaTestConfigDao.COMMAND)
    private String command;
    @Column(CoberturaTestConfigDao.REPORT_DIR)
    private String reportDir;

}