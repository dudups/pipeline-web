package com.ezone.devops.pipeline.task;

import com.ezone.devops.measure.service.MeasureService;
import com.ezone.devops.pipeline.lock.DistributedLock;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class MeasureTask {

    private static final Long LOCK_TIMEOUT_MINUTE = 5L;
    private static final String JOB_MEASURE_KEY = "task:measure:lock";

    @Autowired
    private DistributedLock distributedLock;
    @Autowired
    private MeasureService measureService;
    @Autowired
    private RepoService repoService;

    // 每天半夜执行一次，只统计当前所在的区间
    @Scheduled(cron = "${system.pipeline.measure-cron:0 0 23 * * ?}")
    public void calJobMeasure() {
        boolean lock = distributedLock.lock(JOB_MEASURE_KEY, String.valueOf(true), LOCK_TIMEOUT_MINUTE);
        if (!lock) {
            return;
        }

        List<RepoVo> repos = repoService.getAllRepos();
        if (CollectionUtils.isEmpty(repos)) {
            return;
        }
        log.info("start calc job measure task");
        try {
            for (RepoVo repo : repos) {
                measureService.calcCurrentMeasure(repo.getRepoKey());
            }
        } finally {
            distributedLock.releaseLock(JOB_MEASURE_KEY, String.valueOf(true));
        }

        log.info("finished calc job measure task");
    }


}