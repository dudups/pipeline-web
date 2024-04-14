package com.ezone.devops.plugins.job;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PipelineInitContext {

    private RepoVo repo;
    private PipelineRecord pipelineRecord;
    private JobRecord jobRecord;

}
