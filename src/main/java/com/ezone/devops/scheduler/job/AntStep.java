package com.ezone.devops.scheduler.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AntStep {

    private FileUploadStep fileUploadStep;
    private SpotBugScanStep spotBugScanStep;

}
