package com.ezone.devops.plugins.job.build.gradle.file.dao;

import com.ezone.devops.plugins.job.build.gradle.file.model.GradleConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface GradleConfigDao extends LongKeyBaseDao<GradleConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
