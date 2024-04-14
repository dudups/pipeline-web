package com.ezone.devops.scheduler.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class JenkinsJob {

    private Long companyId;
    private String jenkinsName;
    private String jenkinsJobName;
    private Map<String, String> envs;
    private String callbackUrl;

}
