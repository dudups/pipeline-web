package com.ezone.devops.report.service.impl;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.report.config.ReportSystemConfig;
import com.ezone.devops.report.dao.ReportInfoDao;
import com.ezone.devops.report.model.ReportInfo;
import com.ezone.devops.report.service.ReportInfoService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class ReportInfoServiceImpl implements ReportInfoService {

    private static final String LOG_PATH_FORMAT = "yyyyMMdd";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(LOG_PATH_FORMAT);

    @Autowired
    private ReportSystemConfig reportSystemConfig;
    @Autowired
    private ReportInfoDao reportInfoDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReportInfo createReportInfo(Pipeline pipeline) {
        String reportId = UUID.randomUUID().toString().replaceAll("-", "");
        Long companyId = pipeline.getCompanyId();

        String storagePath = generateReportStoragePath(companyId, reportId);

        ReportInfo reportInfo = new ReportInfo();
        reportInfo.setCompanyId(companyId);
        reportInfo.setStoragePath(storagePath);
        reportInfo.setReportId(reportId);
        reportInfo.setPipelineId(pipeline.getId());
        reportInfo.setRunning(true);

        String storageFullPath = getStorageFullPath(storagePath);
        try {
            FileUtils.deleteDirectory(new File(storageFullPath));
        } catch (IOException e) {
            log.warn("create storage path:[{}] error", storagePath, e);
        }

        reportInfoDao.save(reportInfo);
        return reportInfo;
    }

    private String getStorageFullPath(String storagePath) {
        return reportSystemConfig.getBasePath().concat(File.separator).concat(storagePath);
    }

    @Override
    public ReportInfo getReportInfoIfPresent(String reportId) {
        if (StringUtils.isBlank(reportId)) {
            return null;
        }

        ReportInfo reportInfo = reportInfoDao.getReportInfoByReportId(reportId);
        if (reportInfo == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "报告不存在");
        }
        return reportInfo;
    }

    private String generateReportStoragePath(Long companyId, String reportId) {
        // 1/20200520/{uuid}
        return companyId + File.separator + sdf.format(new Date()) + File.separator + reportId;
    }
}
