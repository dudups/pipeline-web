package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.RuntimeVariable;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.List;

public interface RuntimeVariableDao extends LongKeyBaseDao<RuntimeVariable> {

    String ID = "id";
    String REPO_KEY = "repo_key";
    String BUILD_ID = "build_id";
    String ENV_KEY = "env_key";
    String ENV_VALUE = "env_value";

    List<RuntimeVariable> getByBuildId(Long buildId);

    int deleteByBuildId(Long buildId);

}
