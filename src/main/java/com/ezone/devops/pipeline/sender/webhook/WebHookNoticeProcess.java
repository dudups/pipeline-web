package com.ezone.devops.pipeline.sender.webhook;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.enums.HookPlatform;

public interface WebHookNoticeProcess {

    HookPlatform getHookPlatform();

    String getMessageBody(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord, ResultType resultType);

}
