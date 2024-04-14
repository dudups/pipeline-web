package com.ezone.devops.pipeline.task;

import com.ezone.devops.pipeline.lock.DistributedLock;
import com.ezone.devops.report.config.ReportSystemConfig;
import com.ezone.ezbase.iam.service.IAMCenterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;


@Slf4j
@Component
public class StorageSizeTask {

    private static final Long LOCK_TIMEOUT_MINUTE = 5L;
    private static final String STORAGE_KEY = "task:storage:lock";

    @Autowired
    private DistributedLock distributedLock;
    @Autowired
    private ReportSystemConfig reportSystemConfig;
    @Autowired
    private IAMCenterService iamCenterService;
    @Autowired
    private com.ezone.devops.pipeline.rocketmq.producer.ResourceChargeProducer resourceChargeProducer;

    // 每天半夜执行一次，只统计当前所在的区间
    @Scheduled(cron = "0 30 23 * * ?")
    public void calcReportStorage() {
        boolean lock = distributedLock.lock(STORAGE_KEY, String.valueOf(true), LOCK_TIMEOUT_MINUTE);
        if (!lock) {
            return;
        }

        Long latestId = iamCenterService.queryCompanyLatestId();
        if (latestId == null || latestId == 0L) {
            return;
        }

        log.info("start calc storage size");
        for (long i = 1; i <= latestId; i++) {
            String companyPath = reportSystemConfig.getBasePath() + File.separator + i;
            File file = new File(companyPath);
            if (file.exists()) {
                long sizeOfDirectory = FileUtils.sizeOfDirectory(file);
                if (sizeOfDirectory > 0) {
                    resourceChargeProducer.sendStorageCharge(i, sizeOfDirectory / 1024);
                } else {
                    resourceChargeProducer.sendStorageCharge(i, sizeOfDirectory);
                }
            }
        }
        distributedLock.releaseLock(STORAGE_KEY, String.valueOf(true));
        log.info("finished calc storage size");
    }


}