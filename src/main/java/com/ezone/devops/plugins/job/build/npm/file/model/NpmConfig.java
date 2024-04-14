package com.ezone.devops.plugins.job.build.npm.file.model;

import com.ezone.devops.plugins.job.build.npm.file.dao.NpmConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_npm_config")
public class NpmConfig extends LongID {

    @Column(NpmConfigDao.ID)
    private Long id;
    @Column(NpmConfigDao.DATA_JSON)
    private String dataJson;
}