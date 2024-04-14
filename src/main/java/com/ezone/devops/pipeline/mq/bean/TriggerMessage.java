package com.ezone.devops.pipeline.mq.bean;

import com.ezone.devops.pipeline.common.TriggerMode;
import lombok.Data;

@Data
public class TriggerMessage extends RepoMessage {

    private String branchName;
    private String commitId;
    private String commitMessage;
    private String committer;
    private String operator;
    /**
     * 扩展字段，如分支的推送 ID 或 评审 ID
     */
    private String externalKey;
    /**
     * 跳转地址
     */
    private String dashboardUrl;
    /**
     * 触发事件类型
     */
    private String triggerType;

    private String callbackUrl;

    public TriggerMode getTriggerMode() {
        return TriggerMode.convert(triggerType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("repo(id=").append(getRepoId()).append(", name=").append(getRepoName()).append(")");
        sb.append(", branchName=").append(branchName).append(", externalKey=").append(externalKey);
        sb.append(", newCommit(id=").append(commitId).append(", committer=").append(committer).append(")");
        sb.append(", triggerType=").append(triggerType).append(", operator=").append(operator);
        sb.append("]");
        return sb.toString();
    }

}
