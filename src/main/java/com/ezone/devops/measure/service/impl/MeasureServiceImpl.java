package com.ezone.devops.measure.service.impl;

import com.ezone.devops.ezcode.common.util.DateUtil;
import com.ezone.devops.ezcode.sdk.bean.enums.Dimension;
import com.ezone.devops.measure.beans.*;
import com.ezone.devops.measure.dao.JobMeasureDao;
import com.ezone.devops.measure.model.JobMeasure;
import com.ezone.devops.measure.service.MeasureService;
import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.service.JobRecordService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.PluginType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class MeasureServiceImpl implements MeasureService {

    private static final long DEFAULT_COST_TIME = 0;

    @Autowired
    private JobRecordService jobRecordService;
    @Autowired
    private JobMeasureDao jobMeasureDao;

    @Override
    public Measure getAllMeasureByRepo(String repoKey, Integer year, Dimension dim, Integer value) {
        JobCountGroupByRepoMeasure jobCountGroupByRepoMeasure = getJobCountGroupByRepoQueryByRepo(repoKey, year, dim, value);
        if (jobCountGroupByRepoMeasure == null) {
            jobCountGroupByRepoMeasure = new JobCountGroupByRepoMeasure();
        }
        Collection<JobCountGroupByJobMeasure> jobCountGroupByJobMeasure = getJobCountGroupByJobQueryRepo(repoKey, year, dim, value);
        if (CollectionUtils.isEmpty(jobCountGroupByJobMeasure)) {
            jobCountGroupByJobMeasure = new ArrayList<>();
        }
        List<JobTimeMeasure> jobTimeMeasures = getJobTimeByRepo(repoKey, year, dim, value);
        return new Measure(jobCountGroupByRepoMeasure, jobCountGroupByJobMeasure, jobTimeMeasures);
    }

    @Override
    public JobCountGroupByRepoMeasure getJobCountGroupByRepoQueryByRepo(String repoKey, Integer year, Dimension dim, Integer value) {
        Date startDate = parseStartDate(year, dim, value);
        Date endDate = parseEndDate(year, dim, value);
        List<JobMeasure> jobMeasures = jobMeasureDao.getByCondition(repoKey, startDate, endDate);
        if (CollectionUtils.isEmpty(jobMeasures)) {
            return null;
        }

        JobCountGroupByRepoMeasure jobCountGroupByRepoMeasure = new JobCountGroupByRepoMeasure();
        for (JobMeasure jobMeasure : jobMeasures) {
            if (jobMeasure.isSuccess()) {
                jobCountGroupByRepoMeasure.setSuccess(jobCountGroupByRepoMeasure.getSuccess() + 1);
            } else {
                jobCountGroupByRepoMeasure.setFailed(jobCountGroupByRepoMeasure.getFailed() + 1);
            }
        }

        return jobCountGroupByRepoMeasure;
    }

    @Override
    public Collection<JobCountGroupByJobMeasure> getJobCountGroupByJobQueryRepo(String repoKey, Integer year, Dimension dim, Integer value) {
        Date startDate = parseStartDate(year, dim, value);
        Date endDate = parseEndDate(year, dim, value);
        List<JobMeasure> jobMeasures = jobMeasureDao.getByCondition(repoKey, startDate, endDate);
        if (CollectionUtils.isEmpty(jobMeasures)) {
            return null;
        }

        Map<String, JobCountGroupByJobMeasure> temp = Maps.newHashMap();
        for (JobMeasure jobMeasure : jobMeasures) {
            String jobType = jobMeasure.getJobType();
            if (temp.containsKey(jobType)) {
                JobCountGroupByJobMeasure jobCountGroupByJobMeasure = temp.get(jobType);
                if (jobMeasure.isSuccess()) {
                    jobCountGroupByJobMeasure.setSuccess(jobCountGroupByJobMeasure.getSuccess() + 1);
                } else {
                    jobCountGroupByJobMeasure.setFailed(jobCountGroupByJobMeasure.getFailed() + 1);
                }
            } else {
                JobCountGroupByJobMeasure jobCountGroupByJobMeasure = new JobCountGroupByJobMeasure();
                jobCountGroupByJobMeasure.setJobName(jobType);
                if (jobMeasure.isSuccess()) {
                    jobCountGroupByJobMeasure.setSuccess(1);
                } else {
                    jobCountGroupByJobMeasure.setFailed(1);
                }
                temp.put(jobType, jobCountGroupByJobMeasure);
            }
        }

        return temp.values();
    }

    @Override
    public JobCountGroupByPluginTypeMeasure getJobCountGroupByPluginTypeQueryRepo(String repoKey, Integer year, Dimension dim, Integer value) {
        Date startDate = parseStartDate(year, dim, value);
        Date endDate = parseEndDate(year, dim, value);
        List<JobMeasure> jobMeasures = jobMeasureDao.getByCondition(repoKey, PluginType.BUILD, startDate, endDate);
        if (CollectionUtils.isEmpty(jobMeasures)) {
            return null;
        }

        int success = 0;
        long successAverageSecond = 0;
        int failed = 0;
        long failedAverageSecond = 0;
        for (JobMeasure jobMeasure : jobMeasures) {
            if (jobMeasure.isSuccess()) {
                success = success + 1;
                successAverageSecond = successAverageSecond + jobMeasure.getCostTime();
            } else {
                failed = failed + 1;
                failedAverageSecond = failedAverageSecond + jobMeasure.getCostTime();
            }
        }

        JobCountGroupByPluginTypeMeasure result = new JobCountGroupByPluginTypeMeasure();
        result.setPluginType(PluginType.BUILD);

        result.setSuccess(success);
        if (success > 0) {
            result.setSuccessAverageSecond(successAverageSecond / success / 1000);
        }

        result.setFailed(failed);
        if (failed > 0) {
            result.setFailedAverageSecond(failedAverageSecond / failed / 1000);
        }

        return result;
    }

    @Override
    public List<JobTimeMeasure> getJobTimeByRepo(String repoKey, Integer year, Dimension dim, Integer value) {
        Date startDate = parseStartDate(year, dim, value);
        Date endDate = parseEndDate(year, dim, value);
        List<JobMeasure> jobMeasures = jobMeasureDao.getByCondition(repoKey, startDate, endDate);
        if (CollectionUtils.isEmpty(jobMeasures)) {
            return null;
        }

        List<JobTimeMeasure> result = Lists.newArrayListWithCapacity(jobMeasures.size());
        for (JobMeasure jobMeasure : jobMeasures) {
            JobTimeMeasure jobTimeMeasure = new JobTimeMeasure();
            jobTimeMeasure.setJobType(jobMeasure.getJobType());
            jobTimeMeasure.setStart(jobMeasure.getStartTime());
            jobTimeMeasure.setEnd(jobMeasure.getEndTime());
            result.add(jobTimeMeasure);
        }

        return result;
    }

    @Override
    public Map<String, JobCountGroupByPluginTypeMeasure> getJobCountGroupByPluginTypeQueryRepo(List<RepoVo> repos, Integer year, Dimension dim, Integer value) {
        if (CollectionUtils.isEmpty(repos)) {
            return null;
        }
        Map<String, JobCountGroupByPluginTypeMeasure> result = new HashMap<>(repos.size());
        for (RepoVo repo : repos) {
            if (StringUtils.isBlank(repo.getRepoName())) {
                continue;
            }

            JobCountGroupByPluginTypeMeasure iobCountGroupByPluginTypeMeasure = getJobCountGroupByPluginTypeQueryRepo(repo.getRepoKey(), year, dim, value);
            result.put(repo.getRepoName(), iobCountGroupByPluginTypeMeasure);
        }

        return result;
    }

    @Override
    public Map<String, JobCountGroupByRepoMeasure> getByRepos(List<RepoVo> repos, Integer year, Dimension dim, Integer value) {
        if (CollectionUtils.isEmpty(repos)) {
            return null;
        }
        Map<String, JobCountGroupByRepoMeasure> result = new HashMap<>(repos.size());
        for (RepoVo repo : repos) {
            if (StringUtils.isBlank(repo.getRepoName())) {
                continue;
            }

            JobCountGroupByRepoMeasure jobCountGroupByRepoMeasure = getJobCountGroupByRepoQueryByRepo(repo.getRepoKey(), year, dim, value);
            result.put(repo.getRepoName(), jobCountGroupByRepoMeasure);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByRepo(RepoVo repo) {
        return jobMeasureDao.deleteByRepoKey(repo.getRepoKey()) > 0;
    }

    @Override
    public void calcCurrentMeasure(String repoKey) {
        Date startTimeOfDay = DateUtil.getStartTimeOfDay(new Date());
        Date endTimeOfDay = DateUtil.getEndTimeOfDay(new Date());

        calcMeasure(repoKey, startTimeOfDay, endTimeOfDay);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void calcMeasure(String repoKey, Date start, Date end) {
        List<BuildStatus> statuses = new ArrayList<>();

        // 成功的数量
        statuses.add(BuildStatus.SUCCESS);
        List<JobRecord> successJobRecords = jobRecordService.getByStatus(repoKey, statuses, start, end);
        if (CollectionUtils.isNotEmpty(successJobRecords)) {
            List<JobMeasure> jobMeasures = Lists.newArrayListWithCapacity(successJobRecords.size());
            for (JobRecord jobRecord : successJobRecords) {
                jobMeasures.add(assembleJobMeasure(repoKey, jobRecord, true));
            }

            jobMeasureDao.save(jobMeasures);
        }

        // 失败的数量
        statuses = new ArrayList<>();
        statuses.add(BuildStatus.FAIL);
        statuses.add(BuildStatus.CANCEL);
        statuses.add(BuildStatus.ABORT);

        List<JobRecord> failedJobRecords = jobRecordService.getByStatus(repoKey, statuses, start, end);
        if (CollectionUtils.isNotEmpty(failedJobRecords)) {
            List<JobMeasure> jobMeasures = Lists.newArrayListWithCapacity(failedJobRecords.size());
            for (JobRecord jobRecord : failedJobRecords) {
                jobMeasures.add(assembleJobMeasure(repoKey, jobRecord, false));
            }

            jobMeasureDao.save(jobMeasures);
        }
    }

    private JobMeasure assembleJobMeasure(String repoKey, JobRecord jobRecord, boolean success) {
        Date createTime = jobRecord.getCreateTime();
        Date modifyTime = jobRecord.getModifyTime();
        long costTime = modifyTime.getTime() - createTime.getTime();

        JobMeasure jobMeasure = new JobMeasure();
        jobMeasure.setRepoKey(repoKey);
        jobMeasure.setJobType(jobRecord.getJobType());
        jobMeasure.setPluginType(jobRecord.getPluginType());
        jobMeasure.setStartTime(createTime);
        jobMeasure.setEndTime(modifyTime);
        jobMeasure.setSuccess(success);
        jobMeasure.setCostTime(costTime < 0 ? DEFAULT_COST_TIME : costTime);

        return jobMeasure;
    }

    private Date parseStartDate(Integer year, Dimension dim, Integer value) {
        if (dim == Dimension.QUARTER) {
            return DateUtil.getFirstDayOfQuarter(year, value);
        } else if (dim == Dimension.MONTH) {
            return DateUtil.getFirstDayOfMonth(year, value);
        } else {
            return DateUtil.getFirstDayOfWeek(year, value);
        }
    }

    private Date parseEndDate(Integer year, Dimension dim, Integer value) {
        if (dim == Dimension.QUARTER) {
            return DateUtil.getLastDayOfQuarter(year, value);
        } else if (dim == Dimension.MONTH) {
            return DateUtil.getLastDayOfMonth(year, value);
        } else {
            return DateUtil.getLastDayOfWeek(year, value);
        }
    }

}
