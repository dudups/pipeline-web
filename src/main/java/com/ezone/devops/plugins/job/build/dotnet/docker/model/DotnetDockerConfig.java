package com.ezone.devops.plugins.job.build.dotnet.docker.model;

import com.ezone.devops.plugins.job.build.dotnet.docker.dao.DotnetDockerConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_dotnet_docker_config")
public class DotnetDockerConfig extends LongID {

    @Column(DotnetDockerConfigDao.ID)
    private Long id;
    @Column(DotnetDockerConfigDao.DATA_JSON)
    private String dataJson;
}