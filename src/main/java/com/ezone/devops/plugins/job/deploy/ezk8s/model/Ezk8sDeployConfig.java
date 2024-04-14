package com.ezone.devops.plugins.job.deploy.ezk8s.model;

import com.ezone.devops.plugins.job.deploy.ezk8s.bean.Ezk8sDeployConfigBean;
import com.ezone.devops.plugins.job.deploy.ezk8s.dao.Ezk8sDeployConfigDao;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "plugin_ezk8s_deploy_config")
@NoArgsConstructor
public class Ezk8sDeployConfig extends LongID {

    @Column(Ezk8sDeployConfigDao.ID)
    private Long id;
    @Column(Ezk8sDeployConfigDao.CLUSTER_NAME)
    private String clusterName;
    @Column(Ezk8sDeployConfigDao.ENV_NAME)
    private String envName;
    @Column(Ezk8sDeployConfigDao.DEPLOY_CONFIG_ID)
    private Long deployConfigId;
    @Column(Ezk8sDeployConfigDao.DEPLOY_INSTANCE_NAME)
    private String deployInstanceName;
    @Column(Ezk8sDeployConfigDao.REPLICAS)
    private int replicas = 1;
    @Column(Ezk8sDeployConfigDao.LABELS)
    private String labels = "{}";
    @Column(Ezk8sDeployConfigDao.NODE_SELECTORS)
    private String nodeSelectors = "{}";
    @Column(Ezk8sDeployConfigDao.VERSION_TYPE)
    private VersionType versionType;
    @Column(Ezk8sDeployConfigDao.CUSTOM_VERSION)
    private String customVersion;

    public Ezk8sDeployConfig(Ezk8sDeployConfigBean ezk8SDeployConfigBean) {
        setClusterName(ezk8SDeployConfigBean.getClusterName());
        setEnvName(ezk8SDeployConfigBean.getEnvName());
        setDeployConfigId(ezk8SDeployConfigBean.getDeployConfigId());
        setDeployInstanceName(ezk8SDeployConfigBean.getDeployInstanceName());
        setReplicas(ezk8SDeployConfigBean.getReplicas());
        setLabels(JsonUtils.toJson(ezk8SDeployConfigBean.getLabels()));
        setNodeSelectors(JsonUtils.toJson(ezk8SDeployConfigBean.getNodeSelectors()));
        setVersionType(ezk8SDeployConfigBean.getVersionType());
        setCustomVersion(ezk8SDeployConfigBean.getCustomVersion());
    }
}