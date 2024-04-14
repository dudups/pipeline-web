package com.ezone.devops.plugins.job.deploy.helmdeployv2.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.job.enums.VersionType;
import lombok.Data;

@Data
public class HelmDeployConfigV2Bean {

    private String clusterKey;
    private String namespace;
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long templateId;
    private VersionType versionType;
    private String customVersion;
}
