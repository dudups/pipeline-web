package com.ezone.devops.scheduler.job;

import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.scheduler.bean.ContainerVolume;
import com.ezone.devops.scheduler.bean.DockerCredential;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CommonJob<T> {
    // 插件类型
    private String jobType;

    // clone代码库的方式
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;

    private List<DockerCredential> dockerCredentials;

    // 挂在的存储卷
    private Set<ContainerVolume> tempDirs;

    // 用户的命令
    private String command;

    // 插件的步骤
    private T steps;
}
