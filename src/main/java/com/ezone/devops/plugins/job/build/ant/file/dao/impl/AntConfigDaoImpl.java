package com.ezone.devops.plugins.job.build.ant.file.dao.impl;

import com.ezone.devops.plugins.job.build.ant.file.dao.AntConfigDao;
import com.ezone.devops.plugins.job.build.ant.file.model.AntConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class AntConfigDaoImpl extends BaseCommonDao<AntConfig> implements AntConfigDao {

}
