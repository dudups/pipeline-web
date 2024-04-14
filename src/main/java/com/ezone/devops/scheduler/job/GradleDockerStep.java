package com.ezone.devops.scheduler.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class GradleDockerStep {

    private DockerBuildStep dockerBuildStep;
    private SpotBugScanStep spotBugScanStep;

}
