package com.ezone.devops.plugins.job.build.go.file.model;

import com.ezone.devops.plugins.job.build.go.file.dao.GoConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_go_config")
public class GoConfig extends LongID {

    @Column(GoConfigDao.ID)
    private Long id;
    @Column(GoConfigDao.DATA_JSON)
    private String dataJson;
}