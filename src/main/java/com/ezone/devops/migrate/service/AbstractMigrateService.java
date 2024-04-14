package com.ezone.devops.migrate.service;

import com.ezone.devops.ezcode.sdk.service.InternalRepoService;
import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.plugins.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMigrateService implements MigrateService {

    @Autowired
    protected RepoService repoService;
    @Autowired
    protected PluginService pluginService;
    @Autowired
    protected JobRecordService jobRecordService;
    @Autowired
    protected JobService jobService;
    @Autowired
    protected StageRecordService stageRecordService;
    @Autowired
    protected StageService stageService;
    @Autowired
    protected InternalRepoService internalRepoService;
    @Autowired
    protected PipelineRecordService pipelineRecordService;
    @Autowired
    protected PipelineService pipelineService;

}
