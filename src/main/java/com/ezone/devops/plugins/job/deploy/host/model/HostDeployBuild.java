package com.ezone.devops.plugins.job.deploy.host.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.deploy.host.dao.HostDeployBuildDao;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

@Data
@Table(name = "plugin_host_deploy_build")
public class HostDeployBuild extends LongID {

    @JSONField(serialize = false)
    @Column(HostDeployBuildDao.ID)
    private Long id;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(HostDeployBuildDao.GROUP_ID)
    private Long groupId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(HostDeployBuildDao.TEMPLATE_ID)
    private Long templateId;
    @Column(HostDeployBuildDao.VERSION_TYPE)
    private VersionType versionType;
    @Column(HostDeployBuildDao.DASHBOARD_URL)
    private String dashboardUrl = StringUtils.EMPTY;
    @Length(min = 1, max = 499)
    @ManualField
    @Column(HostDeployBuildDao.CUSTOM_VERSION)
    private String customVersion = StringUtils.EMPTY;
    @ManualField
    @Column(HostDeployBuildDao.DEPLOY_MESSAGE)
    private String deployMessage = StringUtils.EMPTY;

}