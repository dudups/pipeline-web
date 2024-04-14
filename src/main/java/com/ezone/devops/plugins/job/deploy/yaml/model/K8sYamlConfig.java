package com.ezone.devops.plugins.job.deploy.yaml.model;

import com.ezone.devops.plugins.job.deploy.yaml.dao.K8sYamlConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "plugin_k8s_yaml_config")
@NoArgsConstructor
public class K8sYamlConfig extends LongID {

    @Column(K8sYamlConfigDao.ID)
    private Long id;
    @Column(K8sYamlConfigDao.DATA_JSON)
    private String dataJson;

}