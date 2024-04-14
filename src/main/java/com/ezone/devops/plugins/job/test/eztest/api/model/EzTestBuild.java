package com.ezone.devops.plugins.job.test.eztest.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.job.test.eztest.api.dao.EzTestBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_eztest_build")
public class EzTestBuild extends LongID {

    @JSONField(serialize = false)
    @Column(EzTestBuildDao.ID)
    private Long id;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(EzTestBuildDao.PLAN_ID)
    private Long planId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(EzTestBuildDao.SPACE_KEY)
    private String spaceKey;

}