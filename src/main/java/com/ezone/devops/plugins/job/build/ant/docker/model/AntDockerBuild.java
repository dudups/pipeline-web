package com.ezone.devops.plugins.job.build.ant.docker.model;

import com.ezone.devops.plugins.job.build.ant.docker.dao.AntDockerBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_ant_docker_build")
public class AntDockerBuild extends LongID {

    @Column(AntDockerBuildDao.ID)
    private Long id;
    @Column(AntDockerBuildDao.DATA_JSON)
    private String dataJson;
}