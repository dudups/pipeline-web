package com.ezone.devops.plugins.job.build.python.file.model;

import com.ezone.devops.plugins.job.build.python.file.dao.PythonConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_python_config")
public class PythonConfig extends LongID {

    @Column(PythonConfigDao.ID)
    private Long id;
    @Column(PythonConfigDao.DATA_JSON)
    private String dataJson;
}