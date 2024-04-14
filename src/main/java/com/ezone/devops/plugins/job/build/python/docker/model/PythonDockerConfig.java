package com.ezone.devops.plugins.job.build.python.docker.model;

import com.ezone.devops.plugins.job.build.python.docker.dao.PythonDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_python_docker_config")
public class PythonDockerConfig extends LongID {

    @Column(PythonDockerConfigDao.ID)
    private Long id;
    @Column(PythonDockerConfigDao.DATA_JSON)
    private String dataJson;
}