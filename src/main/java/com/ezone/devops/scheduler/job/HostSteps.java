package com.ezone.devops.scheduler.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostSteps {

    private FileUploadStep fileUploadStep;
    private ReportUploadStep reportUploadStep;

}
