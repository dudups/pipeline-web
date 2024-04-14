package com.ezone.devops.plugins.job.build.host.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.build.host.dao.HostCompileBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Table(name = "plugin_host_compile_build")
public class HostCompileBuild extends LongID {

    @JSONField(serialize = false)
    @Column(HostCompileBuildDao.ID)
    private Long id;
    @ManualField
    @Column(HostCompileBuildDao.REPORT_DASHBOARD_URL)
    private String reportDashboardUrl = StringUtils.EMPTY;
    @JSONField(serialize = false)
    @Column(HostCompileBuildDao.REPORT_ID)
    private String reportId = StringUtils.EMPTY;

}