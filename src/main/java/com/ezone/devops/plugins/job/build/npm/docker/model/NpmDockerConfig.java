package com.ezone.devops.plugins.job.build.npm.docker.model;

import com.ezone.devops.plugins.job.build.npm.docker.dao.NpmDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_npm_docker_config")
public class NpmDockerConfig extends LongID {

    @Column(NpmDockerConfigDao.ID)
    private Long id;
    @Column(NpmDockerConfigDao.DATA_JSON)
    private String dataJson;
}