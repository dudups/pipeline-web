package com.ezone.devops.pipeline.sender.webhook.impl;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.enums.HookPlatform;
import org.springframework.stereotype.Component;

@Component
public class FeiShuWebHookProcess extends AbstractWebHookNoticeProcess {

    @Override
    public HookPlatform getHookPlatform() {
        return HookPlatform.FEI_SHU;
    }

    @Override
    public String getMessageBody(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord,
                                 ResultType resultType) {
        return getContent(repo, pipelineRecord, jobRecord, resultType, FEI_SHU_TEMPLATE);
    }
}
