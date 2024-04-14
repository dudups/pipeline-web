package com.ezone.devops.plugins.job.build.go.file.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.enums.VersionType;
import lombok.Data;

@Data
public class Go {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long buildImageId;
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    private String command;
    private boolean uploadArtifact;
    private String pkgRepo;
    private String pkgName;
    private String artifactPath;

    private boolean useDefaultSetting = true;

    private VersionType versionType = VersionType.SNAPSHOT;
    private String customVersion;

}