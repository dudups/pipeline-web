package com.ezone.devops.migrate.service.crontab;

import com.ezone.devops.migrate.service.AbstractMigrateService;
import com.ezone.devops.pipeline.clients.CrontabClient;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.dao.PipelineTriggerConfigDao;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineTriggerConfig;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.CrontabPayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import com.ezone.galaxy.framework.redis.annotation.KLock;
import com.ezone.galaxy.framework.redis.enums.LockTimeoutStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MigrateCrontabDataService extends AbstractMigrateService {

    @Autowired
    private PipelineTriggerConfigDao pipelineTriggerConfigDao;
    @Autowired
    private CrontabClient crontabClient;

    @Override
    public String getMigrateType() {
        return "migrate_crontab_data";
    }

    @Override
    public String getMigrateDesc() {
        return "迁移历史的定时任务数据";
    }

    @KLock(name = "migrate_crontab_data", leaseTime = 6000, lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST)
    @Override
    public boolean migrate(boolean deleteHistory) {
        log.info("start migrate crontab data");
        List<PipelineTriggerConfig> triggerConfigs = pipelineTriggerConfigDao.getByTriggerMode(TriggerMode.CRONTAB);
        if (CollectionUtils.isEmpty(triggerConfigs)) {
            log.info("crontab data is null");
            return true;
        }

        for (PipelineTriggerConfig triggerConfig : triggerConfigs) {
            Pipeline pipeline = pipelineService.getByIdIfPresent(triggerConfig.getPipelineId());
            if (pipeline == null) {
                continue;
            }

            // 删除旧的
            RepoVo repoVo = repoService.getByRepoKey(pipeline.getCompanyId(), pipeline.getRepoKey());
            crontabClient.deprecatedDeleteCrontab(triggerConfig.getPipelineId(), repoVo.getRepoKey());

            // 创建新的
            CrontabPayload crontabPayload = JsonUtils.toObject(triggerConfig.getCrontab(), CrontabPayload.class);
            BaseResponse<?> response = crontabClient.registerCrontab(pipeline, crontabPayload);
            if (response == null) {
                log.error("create crontab error, response is null");
                continue;
            }
            if (response.isError()) {
                log.error("create crontab failed triggerConfig:[{}],repo:[{}]", triggerConfig, repoVo);
            }
        }

        log.info("finished migrate crontab data");
        return true;
    }

}
