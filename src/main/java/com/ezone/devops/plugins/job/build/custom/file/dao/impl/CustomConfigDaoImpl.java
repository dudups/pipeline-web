package com.ezone.devops.plugins.job.build.custom.file.dao.impl;

import com.ezone.devops.plugins.job.build.custom.file.dao.CustomConfigDao;
import com.ezone.devops.plugins.job.build.custom.file.model.CustomConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class CustomConfigDaoImpl extends BaseCommonDao<CustomConfig> implements CustomConfigDao {

}
