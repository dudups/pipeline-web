package com.ezone.devops.measure.dao;

import com.ezone.devops.measure.model.JobMeasure;
import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Date;
import java.util.List;

public interface JobMeasureDao extends LongKeyBaseDao<JobMeasure> {

    String ID = "id";
    String REPO_KEY = "repo_key";
    String JOB_TYPE = "job_type";
    String PLUGIN_TYPE = "plugin_type";
    String START_TIME = "start_time";
    String END_TIME = "end_time";
    String SUCCESS = "success";
    String COST_TIME = "cost_time";

    List<JobMeasure> getByCondition(String repoKey, Date start, Date end);

    List<JobMeasure> getByCondition(String repoKey, PluginType pluginType, Date start, Date end);

    int deleteByRepoKey(String repoKey);
}
