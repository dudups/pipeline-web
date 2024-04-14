package com.ezone.devops.pipeline.mq.consumer;

import com.ezone.devops.measure.service.MeasureService;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineSetting;
import com.ezone.devops.pipeline.mq.bean.CreateCompanyMessage;
import com.ezone.devops.pipeline.mq.bean.RepoMessage;
import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.framework.mq.annotation.consumer.MessageListener;
import com.ezone.galaxy.framework.mq.annotation.consumer.RocketListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@RocketListener(groupName = "ezpipeline")
public class PipelineConsumer {

    private static final int RECORD_DEFAULT_TIME = 180;
    private static final int REPORT_DEFAULT_TIME = 30;

    @Autowired
    private VersionService versionService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private StageRecordService stageRecordService;
    @Autowired
    private JobRecordService jobRecordService;
    @Autowired
    private MeasureService measureService;
    @Autowired
    private PipelineSettingService pipelineSettingService;

    @MessageListener(topic = "COMPANY", tag = "ADD")
    public void createCompany(CreateCompanyMessage message) {
        log.info("receive create company message: [{}].", message);

        PipelineSetting newPipelineSetting = new PipelineSetting();
        newPipelineSetting.setRecordExpireDay(RECORD_DEFAULT_TIME);
        newPipelineSetting.setReportExpireDay(REPORT_DEFAULT_TIME);
        newPipelineSetting.setCompanyId(message.getId());

        PipelineSetting pipelineSetting = pipelineSettingService.getSetting(message.getId());
        if (pipelineSetting == null) {
            pipelineSettingService.saveSetting(newPipelineSetting);
        }
        log.info("init default setting success: [{}].", message);
    }


    @MessageListener(topic = "REPO", tag = "ADD")
    public void createRepo(RepoMessage message) {
        log.info("receive repo add message: [{}].", message);
        RepoVo repoVo = new RepoVo();
        repoVo.setRepoKey(String.valueOf(message.getRepoId()));
        repoVo.setRepoName(message.getRepoName());
        versionService.initVersion(repoVo);
        log.info("repo add success: [{}].", message);
    }

    @MessageListener(topic = "REPO", tag = "DELETE")
    public void deleteRepo(RepoMessage message) {
        log.info("receive repo delete message: [{}].", message);
        RepoVo repo = new RepoVo();
        repo.setCompanyId(message.getCompanyId());
        repo.setRepoKey(message.getRepoId().toString());
        repo.setRepoName(message.getRepoName());

        List<Pipeline> pipelines = pipelineService.listPipeline(repo);
        if (CollectionUtils.isNotEmpty(pipelines)) {
            for (Pipeline pipeline : pipelines) {
                pipelineService.deletePipeline(pipeline);
                stageRecordService.deleteByPipeline(pipeline);
            }
        }

        versionService.deleteByRepo(repo);
        pipelineRecordService.deleteByRepo(repo);
        jobRecordService.deleteByRepo(repo);
        measureService.deleteByRepo(repo);
    }
}
