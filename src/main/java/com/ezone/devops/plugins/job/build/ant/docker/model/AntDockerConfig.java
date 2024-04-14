package com.ezone.devops.plugins.job.build.ant.docker.model;

import com.ezone.devops.plugins.job.build.ant.docker.dao.AntDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_ant_docker_config")
public class AntDockerConfig extends LongID {

    @Column(AntDockerConfigDao.ID)
    private Long id;
    @Column(AntDockerConfigDao.DATA_JSON)
    private String dataJson;
}