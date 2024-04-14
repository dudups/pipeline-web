package com.ezone.devops.plugins.job.build.go.docker.model;

import com.ezone.devops.plugins.job.build.go.docker.dao.GoDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_go_docker_config")
public class GoDockerConfig extends LongID {

    @Column(GoDockerConfigDao.ID)
    private Long id;
    @Column(GoDockerConfigDao.DATA_JSON)
    private String dataJson;
}