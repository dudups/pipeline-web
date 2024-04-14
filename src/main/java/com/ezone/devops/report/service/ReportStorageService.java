package com.ezone.devops.report.service;

import java.io.InputStream;

public interface ReportStorageService {

    boolean saveReport(String reportId, InputStream inputStream);

    boolean deleteReport(Long companyId, int expireDay);
}
