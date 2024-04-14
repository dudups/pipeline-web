package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.web.request.PipelineTemplatePayload;
import com.ezone.galaxy.framework.common.bean.PageResult;

import java.util.List;

public interface PipelineTemplateService {

    PageResult<List<PipelineTemplatePayload>> suggestTemplate(Long companyId, String name, int pageNumber, int pageSize);

    PipelineTemplatePayload getTemplate(Long companyId, Long id);

    boolean saveTemplate(Long companyId, String createUser, PipelineTemplatePayload payload);

    boolean updateTemplate(Long companyId, Long id, String updater, PipelineTemplatePayload payload);

    boolean deleteTemplate(Long companyId, Long id);

}