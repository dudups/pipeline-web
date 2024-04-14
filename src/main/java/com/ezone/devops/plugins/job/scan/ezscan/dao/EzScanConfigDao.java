package com.ezone.devops.plugins.job.scan.ezscan.dao;

import com.ezone.devops.plugins.job.scan.ezscan.model.EzScanConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface EzScanConfigDao extends LongKeyBaseDao<EzScanConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";

}
