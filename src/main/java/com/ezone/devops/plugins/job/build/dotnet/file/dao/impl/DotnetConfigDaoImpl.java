package com.ezone.devops.plugins.job.build.dotnet.file.dao.impl;

import com.ezone.devops.plugins.job.build.dotnet.file.dao.DotnetConfigDao;
import com.ezone.devops.plugins.job.build.dotnet.file.model.DotnetConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class DotnetConfigDaoImpl extends BaseCommonDao<DotnetConfig> implements DotnetConfigDao {

}
