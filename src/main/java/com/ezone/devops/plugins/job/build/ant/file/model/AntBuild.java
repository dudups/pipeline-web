package com.ezone.devops.plugins.job.build.ant.file.model;

import com.ezone.devops.plugins.job.build.ant.file.dao.AntBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_ant_build")
public class AntBuild extends LongID {

    @Column(AntBuildDao.ID)
    private Long id;
    @Column(AntBuildDao.DATA_JSON)
    private String dataJson;
}