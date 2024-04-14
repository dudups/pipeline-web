package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Notice;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.NoticeConfigPayload;

import java.util.List;

public interface NoticeService {

    void saveNotice(Pipeline pipeline, List<NoticeConfigPayload> noticeConfigs);

    void updateNotice(Pipeline pipeline, List<NoticeConfigPayload> noticeConfigs);

    List<Notice> getNotice(Pipeline pipeline);

    List<NoticeConfigPayload> getNoticeConfigPayload(Pipeline pipeline);

    void asyncNoticeJobStart(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord);

    void asyncNoticeJobSuccess(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord);

    void asyncNoticeJobFailed(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord);

}
