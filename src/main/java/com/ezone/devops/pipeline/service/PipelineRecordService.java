package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.PageResult;

import java.util.Date;
import java.util.List;

public interface PipelineRecordService {

    PipelineRecord getByIdIfPresent(Long pipelineId, Long recordId);

    PipelineRecord getByIdIfPresent(Long id);

    boolean updatePipelineRecord(PipelineRecord pipelineRecord);

    boolean deletePipelineRecord(PipelineRecord pipelineRecord);


    boolean deleteByRepo(RepoVo repo);

    /**
     * 根据 stageConfig 初始化 StageBuild 数据，包括其下的 JobBuild 数据
     */
    PipelineRecord initPipelineRecord(Pipeline pipeline, RepoVo repo, ScmTriggerType scmTriggerType, String externalName,
                                      String commit, String user, TriggerMode mode, String externalKey,
                                      String dashboardUrl, String callbackUrl);

    /**
     * 按照流水线id和触发类型查询最新一条的构建记录（定时触发有变更才触发使用）
     *
     * @param pipeline
     * @param triggerMode
     * @return
     */
    PipelineRecord getLatestPipelineRecord(Pipeline pipeline, TriggerMode triggerMode);

    /**
     * 查询流水线构建记录（内部根据评审id查询构建记录使用）
     *
     * @return
     */
    List<PipelineRecord> getPipelineRecords(Long companyId, String repoKey, Long pipelineId, ScmTriggerType scmTriggerType,
                                            String externalName, String externalKey);

    /**
     * 分页查询流水线构建记录
     *
     * @return
     */
    PageResult<List<PipelineRecord>> getPipelineRecords(RepoVo repo, String username, Long pipelineId, ScmTriggerType scmTriggerType,
                                                        String externalName, String externalKey, String commit, VersionType versionType,
                                                        String version, int pageNumber, int pageSize);

    /**
     * 获取当前流水线构建记录关联的卡片
     */
    BaseResponse<?> getPipelineBuildRelateCards(RepoVo repo, PipelineRecord pipelineRecord, String[] fields);

    /**
     * 获取当前流水线构建记录触发事件的详情
     */
    Object getTriggerModeDetail(PipelineRecord pipelineRecord);

    /**
     * 查询日期以前的执行记录
     *
     * @param companyId
     * @param date
     * @return
     */
    List<PipelineRecord> queryByCondition(Long companyId, Date date, int batchSize);
}
