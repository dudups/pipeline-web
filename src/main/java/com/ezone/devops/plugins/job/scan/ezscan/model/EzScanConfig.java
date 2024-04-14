package com.ezone.devops.plugins.job.scan.ezscan.model;

import com.ezone.devops.plugins.job.scan.ezscan.dao.EzScanConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_ezscan_config")
public class EzScanConfig extends LongID {

    @Column(EzScanConfigDao.ID)
    private Long id;
    @Column(EzScanConfigDao.DATA_JSON)
    private String dataJson;
}