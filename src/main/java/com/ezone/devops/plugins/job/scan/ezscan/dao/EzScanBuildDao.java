package com.ezone.devops.plugins.job.scan.ezscan.dao;

import com.ezone.devops.plugins.job.scan.ezscan.model.EzScanBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface EzScanBuildDao extends LongKeyBaseDao<EzScanBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";

}
