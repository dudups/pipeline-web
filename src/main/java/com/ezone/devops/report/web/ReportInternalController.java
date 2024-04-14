package com.ezone.devops.report.web;

import com.ezone.devops.pipeline.task.StorageSizeTask;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/reports")
public class ReportInternalController {

    @Autowired
    private StorageSizeTask storageSizeTask;

    @GetMapping("/calc")
    public BaseResponse<?> calc() {
        storageSizeTask.calcReportStorage();
        return new BaseResponse<>(HttpCode.SUCCESS);
    }
}
