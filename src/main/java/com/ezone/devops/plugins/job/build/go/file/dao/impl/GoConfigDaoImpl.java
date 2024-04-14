package com.ezone.devops.plugins.job.build.go.file.dao.impl;

import com.ezone.devops.plugins.job.build.go.file.dao.GoConfigDao;
import com.ezone.devops.plugins.job.build.go.file.model.GoConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class GoConfigDaoImpl extends BaseCommonDao<GoConfig> implements GoConfigDao {

}
