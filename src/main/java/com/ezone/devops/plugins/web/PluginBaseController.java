package com.ezone.devops.plugins.web;

import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.plugins.service.PluginService;
import com.ezone.ezbase.iam.service.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class PluginBaseController {

    @Autowired
    protected AuthUtil authUtil;
    @Autowired
    protected JobRecordService jobRecordService;
    @Autowired
    protected StageRecordService stageRecordService;
    @Autowired
    protected PipelineRecordService pipelineRecordService;
    @Autowired
    protected PipelineService pipelineService;
    @Autowired
    protected RepoService repoService;
    @Autowired
    protected PipelinePermissionService pipelinePermissionService;
    @Autowired
    protected PluginService pluginService;
}

