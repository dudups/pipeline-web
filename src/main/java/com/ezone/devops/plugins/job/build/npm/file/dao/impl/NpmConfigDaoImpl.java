package com.ezone.devops.plugins.job.build.npm.file.dao.impl;

import com.ezone.devops.plugins.job.build.npm.file.dao.NpmConfigDao;
import com.ezone.devops.plugins.job.build.npm.file.model.NpmConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class NpmConfigDaoImpl extends BaseCommonDao<NpmConfig> implements NpmConfigDao {

}
