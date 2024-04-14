package com.ezone.devops.plugins.job.test.eztest.performance.model;

import com.ezone.devops.plugins.job.test.eztest.performance.dao.EzTestPerformanceConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_eztest_performance_config")
public class EzTestPerformanceConfig extends LongID {

    @Column(EzTestPerformanceConfigDao.ID)
    private Long id;
    @Column(EzTestPerformanceConfigDao.DATA_JSON)
    private String dataJson;

}