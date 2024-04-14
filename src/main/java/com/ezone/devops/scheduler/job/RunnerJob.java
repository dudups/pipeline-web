package com.ezone.devops.scheduler.job;

import com.ezone.devops.pipeline.enums.ResourceType;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.ezbase.iam.bean.enums.SystemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RunnerJob {

    private boolean sendPrepareLog = true;
    private boolean checkAvailableRunner = true;
    private SystemType systemType;
    private Long companyId;
    private ResourceType resourceType;
    private String resourceName;
    private String username;

    private int jobTimeoutMinute;
    private ExecutorType requiredExecutorType = ExecutorType.COMMON;
    private ImageInfo buildImage;

    private String callbackUrl;
    // 环境变量
    private Map<String, String> envs;

    private CommonJob<?> jobDetail;

}
