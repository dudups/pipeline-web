package com.ezone.devops.plugins.job.deploy.host.dao;

import com.ezone.devops.plugins.job.deploy.host.model.HostDeployConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HostDeployConfigDao extends LongKeyBaseDao<HostDeployConfig> {

    String ID = "id";
    String HOST_GROUP_ID = "host_group_id";
    String TEMPLATE_ID = "template_id";
    String VERSION_TYPE = "version_type";
    String CUSTOM_VERSION = "custom_version";

}
