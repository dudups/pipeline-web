package com.ezone.devops.plugins.job.build.dotnet.file.model;

import com.ezone.devops.plugins.job.build.dotnet.file.dao.DotnetConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_dotnet_config")
public class DotnetConfig extends LongID {

    @Column(DotnetConfigDao.ID)
    private Long id;
    @Column(DotnetConfigDao.DATA_JSON)
    private String dataJson;
}