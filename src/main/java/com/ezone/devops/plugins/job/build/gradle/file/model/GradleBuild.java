package com.ezone.devops.plugins.job.build.gradle.file.model;

import com.ezone.devops.plugins.job.build.gradle.file.dao.GradleBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_gradle_build")
public class GradleBuild extends LongID {

    @Column(GradleBuildDao.ID)
    private Long id;
    @Column(GradleBuildDao.DATA_JSON)
    private String dataJson;
}