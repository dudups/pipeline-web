package com.ezone.devops.pipeline.sender.webhook.impl;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.enums.HookPlatform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommonWebHookProcess extends AbstractWebHookNoticeProcess {

    @Override
    public HookPlatform getHookPlatform() {
        return HookPlatform.CUSTOM;
    }

    @Override
    public String getMessageBody(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord,
                                 ResultType resultType) {
        return "{}";
    }
}
