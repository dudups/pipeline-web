package com.ezone.devops.plugins.job.test.eztest.performance.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.pipeline.enums.ResourceType;
import lombok.Data;

@Data
public class EzTestPerformanceConfigBean {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long spaceId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long suiteId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long envId;

    private boolean useCustomCluster;
    private String resourceName;
    private ResourceType resourceType;

}