package com.ezone.devops.plugins.job.build.maven.file.model;

import com.ezone.devops.plugins.job.build.maven.file.dao.MavenBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_maven_build")
public class MavenBuild extends LongID {

    @Column(MavenBuildDao.ID)
    private Long id;
    @Column(MavenBuildDao.DATA_JSON)
    private String dataJson;
}