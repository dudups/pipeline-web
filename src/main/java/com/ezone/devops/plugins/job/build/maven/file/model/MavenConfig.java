package com.ezone.devops.plugins.job.build.maven.file.model;

import com.ezone.devops.plugins.job.build.maven.file.dao.MavenConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_maven_config")
public class MavenConfig extends LongID {

    @Column(MavenConfigDao.ID)
    private Long id;
    @Column(MavenConfigDao.DATA_JSON)
    private String dataJson;
}