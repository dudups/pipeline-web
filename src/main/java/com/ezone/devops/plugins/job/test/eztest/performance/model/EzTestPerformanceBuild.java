package com.ezone.devops.plugins.job.test.eztest.performance.model;

import com.ezone.devops.plugins.job.test.eztest.performance.dao.EzTestPerformanceBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_eztest_performance_build")
public class EzTestPerformanceBuild extends LongID {

    @Column(EzTestPerformanceBuildDao.ID)
    private Long id;
    @Column(EzTestPerformanceBuildDao.DATA_JSON)
    private String dataJson;

}