package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.PipelineSettingDao;
import com.ezone.devops.pipeline.model.PipelineSetting;
import com.ezone.devops.pipeline.service.PipelineSettingService;
import com.ezone.devops.pipeline.web.request.PipelineSettingPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PipelineSettingServiceImpl implements PipelineSettingService {

    @Autowired
    private PipelineSettingDao pipelineSettingDao;

    @Override
    public PipelineSetting getSetting(Long companyId) {
        return pipelineSettingDao.getByCompanyId(companyId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PipelineSetting saveSetting(PipelineSetting pipelineSetting) {
        return pipelineSettingDao.save(pipelineSetting);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PipelineSetting saveOrUpdateSetting(Long companyId, PipelineSettingPayload payload) {
        PipelineSetting pipelineSetting = pipelineSettingDao.getByCompanyId(companyId);
        if (pipelineSetting == null) {
            pipelineSetting = new PipelineSetting();
            pipelineSetting.setCompanyId(companyId);
            pipelineSetting.setRecordExpireDay(payload.getRecordExpireDay());
            pipelineSetting.setReportExpireDay(payload.getReportExpireDay());
            pipelineSettingDao.save(pipelineSetting);
            return pipelineSetting;
        } else {
            pipelineSetting.setRecordExpireDay(payload.getRecordExpireDay());
            pipelineSetting.setReportExpireDay(payload.getReportExpireDay());
            pipelineSettingDao.update(pipelineSetting);
        }
        return pipelineSetting;
    }

    @Override
    public List<PipelineSetting> listAll() {
        return pipelineSettingDao.findAll();
    }
}
