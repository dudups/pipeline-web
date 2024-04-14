package com.ezone.devops.plugins.job.deploy.helmdeployv2.model;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.dao.HelmDeployV2ConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "plugin_helm_deploy_v2_config")
@NoArgsConstructor
public class HelmDeployV2Config extends LongID {

    @Column(HelmDeployV2ConfigDao.ID)
    private Long id;
    @Column(HelmDeployV2ConfigDao.DATA_JSON)
    private String dataJson;

}