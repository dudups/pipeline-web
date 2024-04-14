package com.ezone.devops.plugins.job.build.cmake.file.model;

import com.ezone.devops.plugins.job.build.cmake.file.dao.CmakeConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_cmake_config")
public class CmakeConfig extends LongID {

    @Column(CmakeConfigDao.ID)
    private Long id;
    @Column(CmakeConfigDao.DATA_JSON)
    private String dataJson;
}