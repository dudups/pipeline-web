package com.ezone.devops.plugins.job.build.gradle.docker.model;

import com.ezone.devops.plugins.job.build.gradle.docker.dao.GradleDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_gradle_docker_config")
public class GradleDockerConfig extends LongID {

    @Column(GradleDockerConfigDao.ID)
    private Long id;
    @Column(GradleDockerConfigDao.DATA_JSON)
    private String dataJson;
}