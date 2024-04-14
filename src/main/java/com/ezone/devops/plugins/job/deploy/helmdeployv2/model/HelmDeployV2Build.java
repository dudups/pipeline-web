package com.ezone.devops.plugins.job.deploy.helmdeployv2.model;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.dao.HelmDeployV2BuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_helm_deploy_v2_build")
public class HelmDeployV2Build extends LongID {

    @Column(HelmDeployV2BuildDao.ID)
    private Long id;
    @Column(HelmDeployV2BuildDao.DATA_JSON)
    private String dataJson;

}