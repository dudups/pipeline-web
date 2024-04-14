package com.ezone.devops.plugins.job.test.junittest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.test.junittest.dao.JunitTestBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "plugin_junit_test_build")
public class JunitTestBuild extends LongID {

    @JSONField(serialize = false)
    @Column(JunitTestBuildDao.ID)
    private Long id;
    @JSONField(serialize = false)
    @Column(JunitTestBuildDao.REPORT_ID)
    private String reportId = StringUtils.EMPTY;

    @Column(JunitTestBuildDao.TEST_SUCCESS)
    private Boolean testSuccess;
    @ManualField
    @Column(JunitTestBuildDao.TESTS)
    private Integer tests;
    @ManualField
    @Column(JunitTestBuildDao.ERRORS)
    private Integer errors;
    @ManualField
    @Column(JunitTestBuildDao.SKIPPED)
    private Integer skipped;
    @ManualField
    @Column(JunitTestBuildDao.FAILURES)
    private Integer failures;
    @ManualField
    @Column(JunitTestBuildDao.SUCCESS_RATE)
    private Float successRate;
    @ManualField
    @Column(JunitTestBuildDao.TIME)
    private String time;

}