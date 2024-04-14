package com.ezone.devops.plugins.job.deploy.ezk8s.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.deploy.ezk8s.dao.Ezk8sDeployBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Table(name = "plugin_ezk8s_deploy_build")
public class Ezk8sDeployBuild extends LongID {

    @JSONField(serialize = false)
    @Column(Ezk8sDeployBuildDao.ID)
    private Long id;
    @ManualField
    @Column(Ezk8sDeployBuildDao.DESCRIPTION)
    private String description = StringUtils.EMPTY;
    @ManualField
    @Column(Ezk8sDeployBuildDao.DASHBOARD_URL)
    private String dashboardUrl = StringUtils.EMPTY;

}