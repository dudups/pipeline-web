package com.ezone.devops.measure.service;

import com.ezone.devops.ezcode.sdk.bean.enums.Dimension;
import com.ezone.devops.measure.beans.*;
import com.ezone.devops.pipeline.vo.RepoVo;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MeasureService {

    Measure getAllMeasureByRepo(String repoKey, Integer year, Dimension dim, Integer value);

    JobCountGroupByRepoMeasure getJobCountGroupByRepoQueryByRepo(String repoKey, Integer year, Dimension dim, Integer value);

    Collection<JobCountGroupByJobMeasure> getJobCountGroupByJobQueryRepo(String repoKey, Integer year, Dimension dim, Integer value);

    JobCountGroupByPluginTypeMeasure getJobCountGroupByPluginTypeQueryRepo(String repoKey, Integer year, Dimension dim, Integer value);

    List<JobTimeMeasure> getJobTimeByRepo(String repoKey, Integer year, Dimension dim, Integer value);

    Map<String, JobCountGroupByPluginTypeMeasure> getJobCountGroupByPluginTypeQueryRepo(List<RepoVo> repos, Integer year, Dimension dim, Integer value);

    Map<String, JobCountGroupByRepoMeasure> getByRepos(List<RepoVo> repos, Integer year, Dimension dim, Integer value);

    boolean deleteByRepo(RepoVo repo);

    void calcCurrentMeasure(String repoKey);

    void calcMeasure(String repoKey, Date start, Date end);

}
