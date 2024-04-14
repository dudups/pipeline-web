package com.ezone.devops.plugins.job.build.php.file.dao.impl;

import com.ezone.devops.plugins.job.build.php.file.dao.PhpConfigDao;
import com.ezone.devops.plugins.job.build.php.file.model.PhpConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class PhpConfigDaoImpl extends BaseCommonDao<PhpConfig> implements PhpConfigDao {

}
