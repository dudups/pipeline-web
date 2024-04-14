package com.ezone.devops.plugins.job.deploy.host.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.job.deploy.host.dao.HostDeployConfigDao;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_host_deploy_config")
public class HostDeployConfig extends LongID {

    @Column(HostDeployConfigDao.ID)
    private Long id;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(HostDeployConfigDao.HOST_GROUP_ID)
    private Long hostGroupId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(HostDeployConfigDao.TEMPLATE_ID)
    private Long templateId;
    @Column(HostDeployConfigDao.VERSION_TYPE)
    private VersionType versionType;
    @Column(HostDeployConfigDao.CUSTOM_VERSION)
    private String customVersion;

}