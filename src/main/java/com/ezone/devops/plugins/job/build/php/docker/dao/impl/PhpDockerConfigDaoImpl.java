package com.ezone.devops.plugins.job.build.php.docker.dao.impl;

import com.ezone.devops.plugins.job.build.php.docker.dao.PhpDockerConfigDao;
import com.ezone.devops.plugins.job.build.php.docker.model.PhpDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class PhpDockerConfigDaoImpl extends BaseCommonDao<PhpDockerConfig> implements PhpDockerConfigDao {

}
