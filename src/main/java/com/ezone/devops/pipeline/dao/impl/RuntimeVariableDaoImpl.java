package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.RuntimeVariableDao;
import com.ezone.devops.pipeline.model.RuntimeVariable;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RuntimeVariableDaoImpl extends BaseCommonDao<RuntimeVariable> implements RuntimeVariableDao {


    @Override
    public List<RuntimeVariable> getByBuildId(Long buildId) {
        return find(match(BUILD_ID, buildId));
    }

    @Override
    public int deleteByBuildId(Long buildId) {
        return delete(match(BUILD_ID, buildId));
    }
}
