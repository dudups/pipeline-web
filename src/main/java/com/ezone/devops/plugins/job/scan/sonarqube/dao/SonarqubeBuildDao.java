package com.ezone.devops.plugins.job.scan.sonarqube.dao;

import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface SonarqubeBuildDao extends LongKeyBaseDao<SonarqubeBuild> {

    String ID = "id";
    String DASHBOARD_URL = "dashboard_url";
    String VULNERABILITIES = "vulnerabilities";
    String BUGS = "bugs";
    String CODE_SMELLS = "code_smells";
    String SECURITY_HOTSPOTS = "security_hotspots";
    String SUPPORTED_SECURITY_HOTSPOT = "supported_security_hotspot";
}
