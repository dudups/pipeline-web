package com.ezone.devops.plugins.dao.impl;

import com.ezone.devops.plugins.dao.BuildImageDao;
import com.ezone.devops.plugins.model.BuildImage;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BuildImageDaoImpl extends BaseCommonDao<BuildImage> implements BuildImageDao {

    @Override
    public List<BuildImage> findByJobType(String jobType) {
        return find(match(JOB_TYPE, jobType));
    }

    @Override
    public BuildImage findByName(String jobType, String displayName) {
        return findOne(match(JOB_TYPE, jobType), match(DISPLAY_NAME, displayName));
    }

    @Override
    public boolean deleteBuildImage(String job, Long id) {
        return delete(match(JOB_TYPE, job), match(ID, id)) > 0;
    }
}
