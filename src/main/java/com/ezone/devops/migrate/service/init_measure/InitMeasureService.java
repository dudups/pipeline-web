package com.ezone.devops.migrate.service.init_measure;

import com.ezone.devops.ezcode.common.util.DateUtil;
import com.ezone.devops.measure.service.MeasureService;
import com.ezone.devops.migrate.service.AbstractMigrateService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.framework.redis.annotation.KLock;
import com.ezone.galaxy.framework.redis.enums.LockTimeoutStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class InitMeasureService extends AbstractMigrateService {

    private static final int START_YEAR = 2020;
    @Autowired
    private MeasureService measureService;
    @Autowired
    private RepoService repoService;

    @Override
    public String getMigrateType() {
        return "INIT_MEASURE";
    }

    @Override
    public String getMigrateDesc() {
        return "初始化统计的数据";
    }

    @KLock(name = "init_measure", leaseTime = 6000, lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST)
    @Override
    public boolean migrate(boolean deleteHistory) {
        List<RepoVo> repos = repoService.getAllRepos();
        if (CollectionUtils.isEmpty(repos)) {
            return false;
        }
        log.info("start init job measure data");
        for (RepoVo repo : repos) {
            // 获取当前年
            int curDateYear = DateUtil.getCurYear();
            for (int i = START_YEAR; i <= curDateYear; i++) {
                int allMonths = 12;
                for (int j = 1; j <= allMonths; j++) {
                    Date firstDayOfMonth = DateUtil.getFirstDayOfMonth(i, j);
                    Date lastDayOfMonth = DateUtil.getLastDayOfMonth(i, j);
                    measureService.calcMeasure(repo.getRepoKey(), firstDayOfMonth, lastDayOfMonth);
                }
            }
        }

        log.info("finished init job measure data");
        return true;
    }

}
