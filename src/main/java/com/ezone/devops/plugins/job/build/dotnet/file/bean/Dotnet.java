package com.ezone.devops.plugins.job.build.dotnet.file.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.enums.VersionType;
import lombok.Data;

import java.util.Set;

@Data
public class Dotnet {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long buildImageId;
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    private String command;
    private boolean uploadArtifact;
    private String pkgRepo;
    private String pkgName;
    private String artifactPath;

    private boolean autoGenerateConfig;
    private String userHomePath;
    private Set<String> publicRepoNames;
    private Set<String> privateRepoNames;

    private VersionType versionType = VersionType.SNAPSHOT;
    private String customVersion;

}