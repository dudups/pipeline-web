package com.ezone.devops.plugins.job.build.cmake.docker.model;

import com.ezone.devops.plugins.job.build.cmake.docker.dao.CmakeDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_cmake_docker_config")
public class CmakeDockerConfig extends LongID {

    @Column(CmakeDockerConfigDao.ID)
    private Long id;
    @Column(CmakeDockerConfigDao.DATA_JSON)
    private String dataJson;
}