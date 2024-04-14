package com.ezone.devops.plugins.job.build.maven.docker.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.enums.ScanLevel;
import com.ezone.devops.plugins.job.enums.ArchType;
import com.ezone.devops.plugins.job.enums.PlatformType;
import com.ezone.devops.plugins.job.enums.RegistryType;
import com.ezone.devops.plugins.job.enums.VersionType;
import lombok.Data;

import java.util.Set;

@Data
public class MavenDockerConfigBean {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long buildImageId;
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    private String command;
    private RegistryType pushRegistryType;
    private String dockerfile;
    private String dockerContext;
    private String dockerRepo;
    private String dockerImageName;
    private VersionType dockerVersionType;
    private String dockerTag;
    private PlatformType platform;
    private ArchType arch;

    private boolean autoGenerateConfig;
    private String userHomePath;
    private Set<String> publicRepoNames;
    private Set<String> privateRepoNames;

    private boolean enableScan;
    private String outputPath;
    private Long rulesetId;
    private ScanLevel scanLevel;
    private Set<String> filterDirs;
    private String scanPkgNames;
}