package com.ezone.devops.pipeline.mq.producer;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.MessageValueVo;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.BaseUser;
import com.ezone.ezbase.iam.bean.GroupUser;
import com.ezone.ezbase.iam.bean.HomeAddressDetail;
import com.ezone.ezbase.iam.bean.NotificationMsg;
import com.ezone.ezbase.iam.bean.enums.NotificationType;
import com.ezone.ezbase.iam.bean.enums.SystemType;
import com.ezone.ezbase.iam.bean.setting.MemberSetting;
import com.ezone.ezbase.iam.service.IAMCenterService;
import com.ezone.galaxy.framework.mq.annotation.producer.CommonMessage;
import com.ezone.galaxy.framework.mq.annotation.producer.RocketMessage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;
import java.util.Set;

@Slf4j
@RocketMessage(groupName = "ezpipeline")
public class NotificationMsgProducer {

    private static final String MAIL_TEMPLATE = "mail_notice.ftl";
    private static final String SYSTEM_TEMPLATE = "system_notice.ftl";
    private static final String FEISHU_TEMPLATE = "feishu.json";
    private static final String SENDER = "";

    @Autowired
    private Configuration configuration;
    @Autowired
    private IAMCenterService iamCenterService;

    @CommonMessage(topic = "NOTIFICATION", tag = "SEND")
    public NotificationMsg sendMessageToSystem(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord,
                                               ResultType resultType, Set<GroupUser> receivers) {
        StringWriter output = new StringWriter();
        try {
            MessageValueVo data = getData(repo, pipelineRecord, jobRecord, resultType);
            Template template = configuration.getTemplate(SYSTEM_TEMPLATE);
            template.process(data, output);
            String content = output.toString();
            return mewMessage(repo, receivers, NotificationType.SYSTEM, content);
        } catch (Exception e) {
            log.error("parse system msg ftl error", e);
        }

        return null;
    }

    @CommonMessage(topic = "NOTIFICATION", tag = "SEND")
    public NotificationMsg sendMessageToEmail(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord,
                                              ResultType resultType, Set<GroupUser> receivers) {
        StringWriter output = new StringWriter();
        try {
            MessageValueVo data = getData(repo, pipelineRecord, jobRecord, resultType);
            Template template = configuration.getTemplate(MAIL_TEMPLATE);
            template.process(data, output);
            String content = output.toString();
            return mewMessage(repo, receivers, NotificationType.EMAIL, content);
        } catch (Exception e) {
            log.error("parse email ftl error", e);
        }

        return null;
    }

    @CommonMessage(topic = "NOTIFICATION", tag = "SEND")
    public NotificationMsg sendMessageToFeishu(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord,
                                               ResultType resultType, Set<GroupUser> receivers) {
        StringWriter output = new StringWriter();
        try {
            MessageValueVo data = getData(repo, pipelineRecord, jobRecord, resultType);
            Template template = configuration.getTemplate(FEISHU_TEMPLATE);
            template.process(data, output);
            String content = output.toString();
            return mewMessage(repo, receivers, NotificationType.FEI_SHU, content);
        } catch (Exception e) {
            log.error("parse feishu ftl error", e);
        }

        return null;
    }

    private NotificationMsg mewMessage(RepoVo repo, Set<GroupUser> receivers, NotificationType notificationType, String content) {
        return new NotificationMsg()
                .setCompanyId(repo.getCompanyId())
                .setSystemType(SystemType.EZPIPELINE)
                .setSender(SENDER)
                .setReceivers(receivers)
                .setNotificationType(notificationType)
                .setContent(content);
    }

    private MessageValueVo getData(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord, ResultType resultType) {
        BaseUser baseUser = iamCenterService.queryUserByUsername(jobRecord.getTriggerUser());
        MemberSetting memberSetting = iamCenterService.getMemberSettingByCompanyId(repo.getCompanyId());
        HomeAddressDetail homeAddressDetail = iamCenterService.queryHomeAddressDetailByCompanyId(repo.getCompanyId());
        MessageValueVo messageValueVo = new MessageValueVo();
        messageValueVo.setHomeFullAddress(homeAddressDetail.getHomeFullAddress());
        messageValueVo.setRepoName(repo.getRepoName());
        messageValueVo.setPipelineId(String.valueOf(pipelineRecord.getPipelineId()));
        messageValueVo.setPipelineName(pipelineRecord.getPipelineName());
        messageValueVo.setPipelineBuildId(String.valueOf(pipelineRecord.getId()));
        messageValueVo.setBuildNumber(String.valueOf(pipelineRecord.getBuildNumber()));
        messageValueVo.setJobName(jobRecord.getName());
        messageValueVo.setTriggerUser(jobRecord.getTriggerUser());
        messageValueVo.setNickname(baseUser.getNickname());
        if (memberSetting.isEnableNickname()) {
            messageValueVo.setNickname(baseUser.getNickname());
        } else {
            messageValueVo.setNickname(jobRecord.getTriggerUser());
        }
        messageValueVo.setResultText(resultType.getText());
        return messageValueVo;
    }
}
