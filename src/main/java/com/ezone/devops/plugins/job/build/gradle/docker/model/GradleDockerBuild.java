package com.ezone.devops.plugins.job.build.gradle.docker.model;

import com.ezone.devops.plugins.job.build.gradle.docker.dao.GradleDockerBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_gradle_docker_build")
public class GradleDockerBuild extends LongID {

    @Column(GradleDockerBuildDao.ID)
    private Long id;
    @Column(GradleDockerBuildDao.DATA_JSON)
    private String dataJson;
}