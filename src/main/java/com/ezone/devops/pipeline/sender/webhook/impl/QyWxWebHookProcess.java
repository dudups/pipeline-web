package com.ezone.devops.pipeline.sender.webhook.impl;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.enums.HookPlatform;
import com.ezone.ezbase.iam.bean.hook.QyWxMarkdown;
import com.ezone.ezbase.iam.bean.hook.RobotMarkdownRequestBody;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import org.springframework.stereotype.Component;

@Component
public class QyWxWebHookProcess extends AbstractWebHookNoticeProcess {

    @Override
    public HookPlatform getHookPlatform() {
        return HookPlatform.QY_WX;
    }

    @Override
    public String getMessageBody(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord,
                                 ResultType resultType) {
        RobotMarkdownRequestBody robotBody = new RobotMarkdownRequestBody();
        robotBody.setMarkdown(newMarkdown(repo, pipelineRecord, jobRecord, resultType));
        return JsonUtils.toJson(robotBody);
    }

    private QyWxMarkdown newMarkdown(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord, ResultType resultType) {
        QyWxMarkdown markdown = new QyWxMarkdown();
        String content = getContent(repo, pipelineRecord, jobRecord, resultType, THIRD_PARTY_NOTICE);
        markdown.setContent(content);
        return markdown;
    }
}
