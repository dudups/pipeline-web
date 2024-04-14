package com.ezone.devops.pipeline.sender.webhook.impl;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.enums.HookPlatform;
import com.ezone.ezbase.iam.bean.hook.DingTalkMarkdown;
import com.ezone.ezbase.iam.bean.hook.RobotMarkdownRequestBody;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DingTalkWebHookProcess extends AbstractWebHookNoticeProcess {

    @Override
    public HookPlatform getHookPlatform() {
        return HookPlatform.DING_TALK;
    }

    @Override
    public String getMessageBody(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord,
                                 ResultType resultType) {
        RobotMarkdownRequestBody robotBody = new RobotMarkdownRequestBody();
        robotBody.setMarkdown(newMarkdown(repo, pipelineRecord, jobRecord, resultType));
        return JsonUtils.toJson(robotBody);
    }

    private DingTalkMarkdown newMarkdown(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord,
                                         ResultType resultType) {
        DingTalkMarkdown markdown = new DingTalkMarkdown();
        String content = getContent(repo, pipelineRecord, jobRecord, resultType, THIRD_PARTY_NOTICE);
        markdown.setTitle(jobRecord.getName());
        markdown.setText(content);
        return markdown;
    }
}
