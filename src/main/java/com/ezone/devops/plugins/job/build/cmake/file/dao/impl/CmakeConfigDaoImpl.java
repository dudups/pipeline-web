package com.ezone.devops.plugins.job.build.cmake.file.dao.impl;

import com.ezone.devops.plugins.job.build.cmake.file.dao.CmakeConfigDao;
import com.ezone.devops.plugins.job.build.cmake.file.model.CmakeConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class CmakeConfigDaoImpl extends BaseCommonDao<CmakeConfig> implements CmakeConfigDao {

}
