package com.ezone.devops.plugins.job.other.docker.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.other.docker.dao.DockerExecutorBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "plugin_docker_executor_build")
public class DockerExecutorBuild extends LongID {

    @JSONField(serialize = false)
    @Column(DockerExecutorBuildDao.ID)
    private Long id;

    @ManualField
    @Column(DockerExecutorBuildDao.REPORT_DASHBOARD_URL)
    private String reportDashboardUrl = StringUtils.EMPTY;
    @JSONField(serialize = false)
    @Column(DockerExecutorBuildDao.REPORT_ID)
    private String reportId = StringUtils.EMPTY;

    @Column(DockerExecutorBuildDao.VERSION)
    private String version = StringUtils.EMPTY;

}