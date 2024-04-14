package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.PipelinePayload;
import com.ezone.devops.pipeline.web.response.PipelineVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PipelineService {

    /**
     * 处理前端回传的json串
     *
     * @return 表示处理结果的信息
     */
    Pipeline savePipeline(String repoName, PipelinePayload payload, String username);

    Pipeline updatePipeline(RepoVo repo, Pipeline pipeline, PipelinePayload payload, String username);

    Pipeline updatePipeline(Pipeline pipeline);


    Map<String,List<PipelineVo>> getByRepoKeys(Set<String > repoKeys);

    Pipeline getByIdIfPresent(Long pipelineId);

    /**
     * 查询代码库下关联的流水线
     */
    Map<Long, Pipeline> getPipelines(RepoVo repo);

    List<Pipeline> listPipeline(RepoVo repo);

    /**
     * 通过流水线名称，查询未删除的流水线
     */
    Pipeline getPipeline(RepoVo repo, String pipelineName);

    /**
     * 获取流水线的详细配置信息
     */
    PipelinePayload getPipelinePayload(Long id);

    /**
     * 查询企业下流水线的条数
     *
     * @param companyId
     * @return
     */
    int countByCompanyId(Long companyId);

    boolean deletePipeline(Pipeline pipeline);

    List<Pipeline> getAuthorizedPipeline(RepoVo repo, String username);

    List<Pipeline> getAuthorizedPipeline(RepoVo repo, String username, String pipelineName);

}
