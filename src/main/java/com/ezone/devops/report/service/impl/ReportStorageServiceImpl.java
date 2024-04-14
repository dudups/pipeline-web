package com.ezone.devops.report.service.impl;

import com.ezone.devops.report.config.ReportSystemConfig;
import com.ezone.devops.report.model.ReportInfo;
import com.ezone.devops.report.service.ReportInfoService;
import com.ezone.devops.report.service.ReportStorageService;
import com.ezone.devops.report.utils.UnGZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class ReportStorageServiceImpl implements ReportStorageService {

    private static long ONE_DAY = 1 * 24 * 60 * 60 * 1000;

    @Autowired
    private ReportSystemConfig reportSystemConfig;
    @Autowired
    private ReportInfoService reportInfoService;

    @Override
    public boolean saveReport(String reportId, InputStream inputStream) {
        ReportInfo reportInfo = reportInfoService.getReportInfoIfPresent(reportId);
        String storagePath = reportInfo.getStoragePath();
        String storageFullPath = getStorageFullPath(storagePath);
        try {
            FileUtils.forceMkdir(new File(storageFullPath));
        } catch (IOException e) {
            log.warn("create storage path:[{}] error", storagePath, e);
        }

        String filePath = storageFullPath.concat(File.separator).concat(reportId);
        File dest = new File(filePath);
        try {
            log.info("start write file to [{}]", filePath);
            FileUtils.copyInputStreamToFile(inputStream, dest);
            UnGZipUtil.decompress(filePath, storageFullPath);
            FileUtils.forceDelete(dest);
            log.info("finished write file to [{}]", filePath);
            return true;
        } catch (IOException e) {
            log.error("write file error", e);
            return false;
        }
    }

    @Override
    public boolean deleteReport(Long companyId, int expireDay) {
        // /app/data/1
        String companyDir = reportSystemConfig.getBasePath() + File.separator + String.valueOf(companyId);
        File file = new File(companyDir);
        if (!file.exists()) {
            return false;
        }
        File[] files = file.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            return false;
        }

        long currentTimeMillis = System.currentTimeMillis();
        for (File currentFile : files) {
            long lastModified = currentFile.lastModified();
            long purgeTime = (currentTimeMillis - lastModified) / ONE_DAY;
            if (purgeTime >= expireDay) {
                if (currentFile.isDirectory()) {
                    try {
                        FileUtils.deleteDirectory(currentFile);
                    } catch (IOException e) {
                        // ignore
                        log.error("delete folder error", e);
                    }
                } else {
                    currentFile.delete();
                }
            }
        }
        return true;
    }

    private String getStorageFullPath(String storagePath) {
        return reportSystemConfig.getBasePath().concat(File.separator).concat(storagePath);
    }
}
