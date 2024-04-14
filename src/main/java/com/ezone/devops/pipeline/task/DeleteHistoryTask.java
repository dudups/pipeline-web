package com.ezone.devops.pipeline.task;

import com.ezone.devops.ezcode.common.util.DateUtil;
import com.ezone.devops.pipeline.lock.DistributedLock;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.PipelineSetting;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.service.PipelineSettingService;
import com.ezone.devops.report.service.ReportStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Slf4j
@Component
public class DeleteHistoryTask {

    private static final int BATCH_SIZE = 2000;
    private static final Long LOCK_TIMEOUT_MINUTE = 5L;
    private static final String DELETE_REPORT_KEY = "task:delete_history:lock";

    @Autowired
    private DistributedLock distributedLock;
    @Autowired
    private PipelineSettingService pipelineSettingService;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private ReportStorageService reportStorageService;

    @Scheduled(cron = "${system.pipeline.delete-history-cron:0 0 2 * * ?}")
    public void deleteHistoryData() {
        boolean lock = distributedLock.lock(DELETE_REPORT_KEY, String.valueOf(true), LOCK_TIMEOUT_MINUTE);
        if (!lock) {
            return;
        }

        List<PipelineSetting> pipelineSettings = pipelineSettingService.listAll();
        if (CollectionUtils.isEmpty(pipelineSettings)) {
            return;
        }

        Date endTimeOfDay = DateUtil.getEndTimeOfDay(new Date());
        for (PipelineSetting pipelineSetting : pipelineSettings) {
            Long companyId = pipelineSetting.getCompanyId();
            Date date = getDateBefore(endTimeOfDay, pipelineSetting.getRecordExpireDay());

            List<PipelineRecord> pipelineRecords = pipelineRecordService.queryByCondition(companyId, date, BATCH_SIZE);
            log.info("start delete company [{}] pipeline record task", companyId);
            while (CollectionUtils.isNotEmpty(pipelineRecords)) {
                PipelineRecord first = pipelineRecords.get(0);
                PipelineRecord end = pipelineRecords.get(pipelineRecords.size() - 1);
                log.info("delete company {} pipeline record start id {} end id {}", companyId, first.getId(), end.getId());
                for (PipelineRecord pipelineRecord : pipelineRecords) {
                    pipelineRecordService.deletePipelineRecord(pipelineRecord);
                }

                pipelineRecords = pipelineRecordService.queryByCondition(companyId, date, BATCH_SIZE);
            }
            log.info("finished delete company [{}] pipeline record task", companyId);
        }

        for (PipelineSetting pipelineSetting : pipelineSettings) {
            Long companyId = pipelineSetting.getCompanyId();
            int reportExpireDay = pipelineSetting.getReportExpireDay();
            log.info("start delete company [{}] report task", companyId);
            reportStorageService.deleteReport(companyId, reportExpireDay);
            log.info("finished delete company [{}] report task", companyId);
        }

    }

    public static Date getDateBefore(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();

    }

}