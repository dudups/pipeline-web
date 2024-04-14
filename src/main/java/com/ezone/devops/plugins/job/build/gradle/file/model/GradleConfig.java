package com.ezone.devops.plugins.job.build.gradle.file.model;

import com.ezone.devops.plugins.job.build.gradle.file.dao.GradleConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_gradle_config")
public class GradleConfig extends LongID {

    @Column(GradleConfigDao.ID)
    private Long id;
    @Column(GradleConfigDao.DATA_JSON)
    private String dataJson;
}