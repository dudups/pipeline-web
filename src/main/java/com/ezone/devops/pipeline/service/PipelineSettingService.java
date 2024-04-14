package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.PipelineSetting;
import com.ezone.devops.pipeline.web.request.PipelineSettingPayload;

import java.util.List;

public interface PipelineSettingService {

    PipelineSetting getSetting(Long companyId);

    PipelineSetting saveSetting(PipelineSetting pipelineSetting);

    PipelineSetting saveOrUpdateSetting(Long companyId, PipelineSettingPayload payload);

    List<PipelineSetting> listAll();

}
