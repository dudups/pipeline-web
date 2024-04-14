package com.ezone.devops.plugins.job.build.gradle.file.dao;

import com.ezone.devops.plugins.job.build.gradle.file.model.GradleBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface GradleBuildDao extends LongKeyBaseDao<GradleBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
