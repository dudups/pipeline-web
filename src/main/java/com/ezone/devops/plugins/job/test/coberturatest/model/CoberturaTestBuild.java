package com.ezone.devops.plugins.job.test.coberturatest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.test.coberturatest.dao.CoberturaTestBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "plugin_cobertura_test_build")
public class CoberturaTestBuild extends LongID {

    @JSONField(serialize = false)
    @Column(CoberturaTestBuildDao.ID)
    private Long id;
    @JSONField(serialize = false)
    @Column(CoberturaTestBuildDao.REPORT_ID)
    private String reportId = StringUtils.EMPTY;

    @Column(CoberturaTestBuildDao.TEST_SUCCESS)
    private Boolean testSuccess;
    @ManualField
    @Column(CoberturaTestBuildDao.DASHBOARD_URL)
    private String dashboardUrl;
    @ManualField
    @Column(CoberturaTestBuildDao.LINE_COVERAGE)
    private Float lineCoverage;
    @ManualField
    @Column(CoberturaTestBuildDao.BRANCH_COVERAGE)
    private Float branchCoverage;

}