package com.ezone.devops.plugins.job.other.callpipeline.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.job.other.callpipeline.dao.CallPipelineBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "plugin_call_pipeline_build")
public class CallPipelineBuild extends LongID {

    @JSONField(serialize = false)
    @Column(CallPipelineBuildDao.ID)
    private Long id;
    @Column(CallPipelineBuildDao.DASHBOARD_URL)
    private String dashboardUrl = StringUtils.EMPTY;

}