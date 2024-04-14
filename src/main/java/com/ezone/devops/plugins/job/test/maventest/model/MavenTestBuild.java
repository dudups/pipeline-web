package com.ezone.devops.plugins.job.test.maventest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.test.maventest.dao.MavenTestBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "plugin_maven_test_build")
public class MavenTestBuild extends LongID {

    @JSONField(serialize = false)
    @Column(MavenTestBuildDao.ID)
    private Long id;
    @JSONField(serialize = false)
    @Column(MavenTestBuildDao.REPORT_ID)
    private String reportId = StringUtils.EMPTY;

    @Column(MavenTestBuildDao.TEST_SUCCESS)
    private Boolean testSuccess;
    @Column(MavenTestBuildDao.DASHBOARD_URL)
    private String dashboardUrl;
    @ManualField
    @Column(MavenTestBuildDao.TESTS)
    private Integer tests;
    @ManualField
    @Column(MavenTestBuildDao.ERRORS)
    private Integer errors;
    @ManualField
    @Column(MavenTestBuildDao.FAILURES)
    private Integer failures;
    @ManualField
    @Column(MavenTestBuildDao.SUCCESS_RATE)
    private Float successRate;
    @ManualField
    @Column(MavenTestBuildDao.TIME)
    private String time;

}