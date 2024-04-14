package com.ezone.devops.plugins.job.deploy.host.dao;

import com.ezone.devops.plugins.job.deploy.host.model.HostDeployBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HostDeployBuildDao extends LongKeyBaseDao<HostDeployBuild> {

    String ID = "id";
    String GROUP_ID = "group_id";
    String TEMPLATE_ID = "template_id";
    String VERSION_TYPE = "version_type";
    String CUSTOM_VERSION = "custom_version";
    String DASHBOARD_URL = "dashboard_url";
    String DEPLOY_MESSAGE = "deploy_message";

}
