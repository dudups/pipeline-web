package com.ezone.devops.plugins.job.deploy.helmdeploy.model;

import com.ezone.devops.plugins.job.deploy.helmdeploy.dao.HelmDeployBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_helm_deploy_build")
public class HelmDeployBuild extends LongID {

    @Column(HelmDeployBuildDao.ID)
    private Long id;
    @Column(HelmDeployBuildDao.DATA_JSON)
    private String dataJson;

}