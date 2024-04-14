package com.ezone.devops.measure.dao.impl;

import com.ezone.devops.measure.dao.JobMeasureDao;
import com.ezone.devops.measure.model.JobMeasure;
import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.Params;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class JobMeasureDaoImpl extends BaseCommonDao<JobMeasure> implements JobMeasureDao {

    @Override
    public List<JobMeasure> getByCondition(String repoKey, Date start, Date end) {
        return find(match(REPO_KEY, repoKey), match(END_TIME, Params.between(start, end)));
    }

    @Override
    public List<JobMeasure> getByCondition(String repoKey, PluginType pluginType, Date start, Date end) {
        return find(match(REPO_KEY, repoKey), match(PLUGIN_TYPE, pluginType), match(END_TIME, Params.between(start, end)));
    }

    @Override
    public int deleteByRepoKey(String repoKey) {
        return delete(match(REPO_KEY, repoKey));
    }
}
