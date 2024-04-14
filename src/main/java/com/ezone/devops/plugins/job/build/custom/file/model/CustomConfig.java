package com.ezone.devops.plugins.job.build.custom.file.model;

import com.ezone.devops.plugins.job.build.custom.file.dao.CustomConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_custom_config")
public class CustomConfig extends LongID {

    @Column(CustomConfigDao.ID)
    private Long id;
    @Column(CustomConfigDao.DATA_JSON)
    private String dataJson;
}