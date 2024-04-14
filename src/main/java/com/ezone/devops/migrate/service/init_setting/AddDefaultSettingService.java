package com.ezone.devops.migrate.service.init_setting;

import com.ezone.devops.migrate.service.AbstractMigrateService;
import com.ezone.devops.pipeline.dao.PipelineSettingDao;
import com.ezone.devops.pipeline.model.PipelineSetting;
import com.ezone.ezbase.iam.service.IAMCenterService;
import com.ezone.galaxy.framework.redis.annotation.KLock;
import com.ezone.galaxy.framework.redis.enums.LockTimeoutStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddDefaultSettingService extends AbstractMigrateService {

    private static final int RECORD_DEFAULT_TIME = 180;
    private static final int REPORT_DEFAULT_TIME = 30;
    @Autowired
    private IAMCenterService iamCenterService;
    @Autowired
    private PipelineSettingDao pipelineSettingDao;

    @Override
    public String getMigrateType() {
        return "add_default_setting";
    }

    @Override
    public String getMigrateDesc() {
        return "给企业设置添加默认值";
    }

    @KLock(name = "add_default_setting", leaseTime = 6000, lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST)
    @Override
    public boolean migrate(boolean deleteHistory) {
        Long latestCompanyId = iamCenterService.queryCompanyLatestId();
        if (latestCompanyId == null || latestCompanyId == 0L) {
            return true;
        }

        log.info("start init default setting");
        for (long i = 1; i <= latestCompanyId; i++) {
            PipelineSetting newPipelineSetting = new PipelineSetting();
            newPipelineSetting.setRecordExpireDay(RECORD_DEFAULT_TIME);
            newPipelineSetting.setReportExpireDay(REPORT_DEFAULT_TIME);
            newPipelineSetting.setCompanyId(i);

            PipelineSetting pipelineSetting = pipelineSettingDao.getByCompanyId(i);
            if (pipelineSetting == null) {
                pipelineSettingDao.save(newPipelineSetting);
            }
        }
        log.info("finished init default setting");
        return true;
    }

}
