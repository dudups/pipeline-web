package com.ezone.devops.pipeline.sender.webhook.impl;

import com.ezone.devops.pipeline.config.SystemConfig;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.MessageValueVo;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.sender.webhook.WebHookNoticeProcess;
import com.ezone.ezbase.iam.bean.BaseUser;
import com.ezone.ezbase.iam.bean.HomeAddressDetail;
import com.ezone.ezbase.iam.bean.setting.MemberSetting;
import com.ezone.ezbase.iam.service.IAMCenterService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;

@Slf4j
public abstract class AbstractWebHookNoticeProcess implements WebHookNoticeProcess {

    @Autowired
    protected SystemConfig systemConfig;
    @Autowired
    protected Configuration configuration;
    @Autowired
    protected IAMCenterService iamCenterService;

    protected static final String THIRD_PARTY_NOTICE = "third_party_notice.ftl";
    protected static final String FEI_SHU_TEMPLATE = "feishu.json";

    protected MessageValueVo getData(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord, ResultType resultType) {
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
        messageValueVo.setResultText(resultType.getText());

        if (memberSetting.isEnableNickname()) {
            messageValueVo.setNickname(baseUser.getNickname());
        } else {
            messageValueVo.setNickname(jobRecord.getTriggerUser());
        }
        return messageValueVo;
    }

    protected String getContent(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord, ResultType resultType,
                                String fltName) {
        StringWriter output = new StringWriter();
        try {
            MessageValueVo data = getData(repo, pipelineRecord, jobRecord, resultType);
            Template template = configuration.getTemplate(fltName);
            template.process(data, output);
            return output.toString();
        } catch (Exception e) {
            log.error("parse ftl error", e);
        }
        return null;
    }

}
