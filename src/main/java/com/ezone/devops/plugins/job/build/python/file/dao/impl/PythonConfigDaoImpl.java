package com.ezone.devops.plugins.job.build.python.file.dao.impl;

import com.ezone.devops.plugins.job.build.python.file.dao.PythonConfigDao;
import com.ezone.devops.plugins.job.build.python.file.model.PythonConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class PythonConfigDaoImpl extends BaseCommonDao<PythonConfig> implements PythonConfigDao {

}
