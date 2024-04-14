package com.ezone.devops.plugins.job.build.php.docker.model;

import com.ezone.devops.plugins.job.build.php.docker.dao.PhpDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_php_docker_config")
public class PhpDockerConfig extends LongID {

    @Column(PhpDockerConfigDao.ID)
    private Long id;
    @Column(PhpDockerConfigDao.DATA_JSON)
    private String dataJson;
}