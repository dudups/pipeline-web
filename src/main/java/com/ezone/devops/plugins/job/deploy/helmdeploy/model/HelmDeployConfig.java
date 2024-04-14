package com.ezone.devops.plugins.job.deploy.helmdeploy.model;

import com.ezone.devops.plugins.job.deploy.helmdeploy.dao.HelmDeployConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "plugin_helm_deploy_config")
@NoArgsConstructor
public class HelmDeployConfig extends LongID {

    @Column(HelmDeployConfigDao.ID)
    private Long id;
    @Column(HelmDeployConfigDao.DATA_JSON)
    private String dataJson;

}