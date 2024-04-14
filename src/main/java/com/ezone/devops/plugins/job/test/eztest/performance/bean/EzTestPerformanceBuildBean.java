package com.ezone.devops.plugins.job.test.eztest.performance.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.annotation.ManualField;
import lombok.Data;

@Data
public class EzTestPerformanceBuildBean {

    @ManualField
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long planId;
    @ManualField
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long planSeqNum;
    @ManualField
    private String spaceKey;


}