package com.ezone.devops.plugins.job.build.ant.file.model;

import com.ezone.devops.plugins.job.build.ant.file.dao.AntConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_ant_config")
public class AntConfig extends LongID {

    @Column(AntConfigDao.ID)
    private Long id;
    @Column(AntConfigDao.DATA_JSON)
    private String dataJson;
}