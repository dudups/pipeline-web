package com.ezone.devops.plugins.job.scan.ezscan.model;

import com.ezone.devops.plugins.job.scan.ezscan.dao.EzScanBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_ezscan_build")
public class EzScanBuild extends LongID {

    @Column(EzScanBuildDao.ID)
    private Long id;
    @Column(EzScanBuildDao.DATA_JSON)
    private String dataJson = "{}";
}