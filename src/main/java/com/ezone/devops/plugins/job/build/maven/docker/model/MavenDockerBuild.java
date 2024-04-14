package com.ezone.devops.plugins.job.build.maven.docker.model;

import com.ezone.devops.plugins.job.build.maven.docker.dao.MavenDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_maven_docker_build")
public class MavenDockerBuild extends LongID {

    @Column(MavenDockerConfigDao.ID)
    private Long id;
    @Column(MavenDockerConfigDao.DATA_JSON)
    private String dataJson;
}