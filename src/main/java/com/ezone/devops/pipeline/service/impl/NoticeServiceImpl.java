package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.NoticeDao;
import com.ezone.devops.pipeline.enums.MemberType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Notice;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.mq.producer.NotificationMsgProducer;
import com.ezone.devops.pipeline.sender.WebHookNoticeSender;
import com.ezone.devops.pipeline.service.NoticeService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.NoticeConfigPayload;
import com.ezone.ezbase.iam.bean.GroupUser;
import com.ezone.ezbase.iam.bean.enums.GroupUserType;
import com.ezone.galaxy.framework.main.concurrent.aop.Pooled;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private NotificationMsgProducer notificationMsgProducer;
    @Autowired
    private WebHookNoticeSender webHookNoticeSender;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNotice(Pipeline pipeline, List<NoticeConfigPayload> noticeConfigPayloads) {
        if (CollectionUtils.isEmpty(noticeConfigPayloads)) {
            return;
        }
        List<Notice> noticeConfigs = Lists.newArrayList();
        noticeConfigPayloads.forEach(noticeConfigBean -> noticeConfigs.add(new Notice(pipeline, noticeConfigBean)));
        noticeDao.save(noticeConfigs);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNotice(Pipeline pipeline, List<NoticeConfigPayload> noticeConfigPayloads) {
        noticeDao.deleteByPipelineId(pipeline.getId());
        if (CollectionUtils.isEmpty(noticeConfigPayloads)) {
            return;
        }
        List<Notice> noticeConfigs = Lists.newArrayList();
        noticeConfigPayloads.forEach(noticeConfigBean -> noticeConfigs.add(new Notice(pipeline, noticeConfigBean)));
        noticeDao.save(noticeConfigs);
    }

    @Override
    public List<Notice> getNotice(Pipeline pipeline) {
        return noticeDao.getNoticeConfigByPipelineId(pipeline.getId());
    }

    @Override
    public List<NoticeConfigPayload> getNoticeConfigPayload(Pipeline pipeline) {
        List<Notice> notices = getNotice(pipeline);
        if (CollectionUtils.isEmpty(notices)) {
            return null;
        }

        List<NoticeConfigPayload> noticeConfigPayloads = Lists.newArrayListWithCapacity(notices.size());
        for (Notice notice : notices) {
            NoticeConfigPayload noticeConfigPayload = new NoticeConfigPayload();
            BeanUtils.copyProperties(notice, noticeConfigPayload);
            noticeConfigPayloads.add(noticeConfigPayload);
        }

        return noticeConfigPayloads;
    }

    public List<Notice> getNotice(PipelineRecord pipelineRecord) {
        return noticeDao.getNoticeConfigByPipelineId(pipelineRecord.getPipelineId());
    }

    @Pooled(timeout = 20000)
    @Override
    public void asyncNoticeJobStart(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        sendToMq(repo, pipelineRecord, jobRecord, ResultType.START);
        webHookNoticeSender.invokeStartHook(repo, pipelineRecord, jobRecord);
    }

    @Pooled(timeout = 20000)
    @Override
    public void asyncNoticeJobSuccess(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        sendToMq(repo, pipelineRecord, jobRecord, ResultType.SUCCESS);
        webHookNoticeSender.invokeSuccessHook(repo, pipelineRecord, jobRecord);
    }

    @Pooled(timeout = 20000)
    @Override
    public void asyncNoticeJobFailed(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        sendToMq(repo, pipelineRecord, jobRecord, ResultType.FAIL);
        webHookNoticeSender.invokeFailedHook(repo, pipelineRecord, jobRecord);
    }

    private void sendToMq(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord, ResultType resultType) {
        List<Notice> notices = getNotice(pipelineRecord);
        if (CollectionUtils.isEmpty(notices)) {
            return;
        }

        Set<GroupUser> receivers = assembleReceivers(notices, resultType, jobRecord);
        notificationMsgProducer.sendMessageToSystem(repo, pipelineRecord, jobRecord, resultType, receivers);
        notificationMsgProducer.sendMessageToEmail(repo, pipelineRecord, jobRecord, resultType, receivers);
        notificationMsgProducer.sendMessageToFeishu(repo, pipelineRecord, jobRecord, resultType, receivers);
    }

    private Set<GroupUser> assembleReceivers(List<Notice> notices, ResultType resultType, JobRecord jobRecord) {
        Set<String> users = Sets.newHashSet();
        Set<String> groups = Sets.newHashSet();

        for (Notice notice : notices) {
            MemberType memberType = notice.getMemberType();
            String name = notice.getName();
            if (resultType == ResultType.START) {
                if (notice.isStart()) {
                    if (memberType == MemberType.SYS_TRIGGER_USER_SELF) {
                        users.add(jobRecord.getTriggerUser());
                        continue;
                    }
                    if (memberType == MemberType.USER) {
                        users.add(name);
                    } else {
                        groups.add(name);
                    }
                }
            } else if (resultType == ResultType.FAIL) {
                if (notice.isFailed()) {
                    if (memberType == MemberType.SYS_TRIGGER_USER_SELF) {
                        users.add(jobRecord.getTriggerUser());
                        continue;
                    }
                    if (memberType == MemberType.USER) {
                        users.add(name);
                    } else {
                        groups.add(name);
                    }
                }
            } else {
                if (notice.isSuccess()) {
                    if (memberType == MemberType.SYS_TRIGGER_USER_SELF) {
                        users.add(jobRecord.getTriggerUser());
                        continue;
                    }
                    if (memberType == MemberType.USER) {
                        users.add(name);
                    } else {
                        groups.add(name);
                    }
                }
            }
        }

        Set<GroupUser> receivers = new HashSet<>(notices.size() + 1);
        if (CollectionUtils.isNotEmpty(users)) {
            for (String user : users) {
                receivers.add(new GroupUser(user, GroupUserType.USER));
            }
        }

        if (CollectionUtils.isNotEmpty(groups)) {
            for (String group : groups) {
                receivers.add(new GroupUser(group, GroupUserType.GROUP));
            }
        }

        return receivers;
    }
}
