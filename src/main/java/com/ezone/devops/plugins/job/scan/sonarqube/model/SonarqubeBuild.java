package com.ezone.devops.plugins.job.scan.sonarqube.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.scan.sonarqube.dao.SonarqubeBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_sonarqube_build")
public class SonarqubeBuild extends LongID {

    @JSONField(serialize = false)
    @Column(SonarqubeBuildDao.ID)
    private Long id;
    @ManualField
    @Column(SonarqubeBuildDao.DASHBOARD_URL)
    private String dashboardUrl;
    @ManualField
    @Column(SonarqubeBuildDao.VULNERABILITIES)
    private Integer vulnerabilities;
    @ManualField
    @Column(SonarqubeBuildDao.BUGS)
    private Integer bugs;
    @ManualField
    @Column(SonarqubeBuildDao.CODE_SMELLS)
    private Integer codeSmells;
    @ManualField
    @Column(SonarqubeBuildDao.SECURITY_HOTSPOTS)
    private Integer securityHotspots;
    @ManualField
    @Column(SonarqubeBuildDao.SUPPORTED_SECURITY_HOTSPOT)
    private boolean supportedSecurityHotspot;
}