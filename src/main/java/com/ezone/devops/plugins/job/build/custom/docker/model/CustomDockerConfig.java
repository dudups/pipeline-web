package com.ezone.devops.plugins.job.build.custom.docker.model;

import com.ezone.devops.plugins.job.build.custom.docker.dao.CustomDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_custom_docker_config")
public class CustomDockerConfig extends LongID {

    @Column(CustomDockerConfigDao.ID)
    private Long id;
    @Column(CustomDockerConfigDao.DATA_JSON)
    private String dataJson;
}