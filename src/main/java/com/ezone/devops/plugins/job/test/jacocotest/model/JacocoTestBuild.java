package com.ezone.devops.plugins.job.test.jacocotest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.test.jacocotest.dao.JacocoTestBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "plugin_jacoco_test_build")
public class JacocoTestBuild extends LongID {

    @JSONField(serialize = false)
    @Column(JacocoTestBuildDao.ID)
    private Long id;
    @JSONField(serialize = false)
    @Column(JacocoTestBuildDao.REPORT_ID)
    private String reportId = StringUtils.EMPTY;

    @Column(JacocoTestBuildDao.TEST_SUCCESS)
    private Boolean testSuccess;
    @Column(JacocoTestBuildDao.DASHBOARD_URL)
    private String dashboardUrl;
    @ManualField
    @Column(JacocoTestBuildDao.INSTRUCTIONS_COVERAGE)
    private Float instructionsCoverage;
    @ManualField
    @Column(JacocoTestBuildDao.BRANCH_COVERAGE)
    private Float branchCoverage;
    @ManualField
    @Column(JacocoTestBuildDao.LINE_COVERAGE)
    private Float lineCoverage;
    @ManualField
    @Column(JacocoTestBuildDao.METHOD_COVERAGE)
    private Float methodCoverage;
    @ManualField
    @Column(JacocoTestBuildDao.CLASS_COVERAGE)
    private Float classCoverage;

}