package com.ezone.devops.report.web;

import com.ezone.devops.report.acl.OpenApi;
import com.ezone.devops.report.service.ReportStorageService;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * agent上传报告用
 */
@RestController
@RequestMapping("/reports/{reportId}")
public class ReportRestController {

    @Autowired
    private ReportStorageService reportStorageService;

    @OpenApi
    @PostMapping
    public BaseResponse<?> uploadReport(@PathVariable String reportId, HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        reportStorageService.saveReport(reportId, inputStream);
        return new BaseResponse<>(HttpCode.SUCCESS);
    }

}
