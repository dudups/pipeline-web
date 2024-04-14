package com.ezone.devops.plugins.job.test.eztest.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.job.test.eztest.api.dao.EzTestConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_eztest_config")
public class EzTestConfig extends LongID {

    @Column(EzTestConfigDao.ID)
    private Long id;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(EzTestConfigDao.ENV_ID)
    private Long envId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(EzTestConfigDao.SUITE_ID)
    private Long suiteId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(EzTestConfigDao.PL_CONF_ID)
    private Long plConfId;

}