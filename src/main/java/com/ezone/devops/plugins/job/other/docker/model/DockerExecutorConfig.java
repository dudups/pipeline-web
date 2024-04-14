package com.ezone.devops.plugins.job.other.docker.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.enums.*;
import com.ezone.devops.plugins.job.other.docker.dao.DockerExecutorConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Table(name = "plugin_docker_executor_config")
public class DockerExecutorConfig extends LongID {

    private static final Long DEFAULT_ARTIFACT_ID = 0L;

    @Column(DockerExecutorConfigDao.ID)
    private Long id;
    @Column(DockerExecutorConfigDao.CLONE_MODE)
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    @Column(DockerExecutorConfigDao.REGISTRY_TYPE)
    private RegistryType registryType;
    @Column(DockerExecutorConfigDao.PROVIDER_NAME)
    private String providerName;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(DockerExecutorConfigDao.REGISTRY_ID)
    private Long registryId;
    @Column(DockerExecutorConfigDao.IMAGE_NAME)
    private String imageName;
    @Column(DockerExecutorConfigDao.VERSION_TYPE)
    private ImageVersionType versionType = ImageVersionType.FIXED;
    @Column(DockerExecutorConfigDao.VERSION)
    private String version;
    @Column(DockerExecutorConfigDao.COMMAND)
    private String command;

    @Column(DockerExecutorConfigDao.UPLOAD_REPORT)
    private boolean uploadReport = false;
    @Column(DockerExecutorConfigDao.REPORT_DIR)
    private String reportDir = StringUtils.EMPTY;
    @Column(DockerExecutorConfigDao.REPORT_INDEX)
    private String reportIndex = StringUtils.EMPTY;

    @Column(DockerExecutorConfigDao.UPLOAD_ARTIFACT)
    private boolean uploadArtifact = false;
    @Column(DockerExecutorConfigDao.PKG_REPO)
    private String pkgRepo = StringUtils.EMPTY;
    @Column(DockerExecutorConfigDao.PKG_NAME)
    private String pkgName = StringUtils.EMPTY;
    @Column(DockerExecutorConfigDao.ARTIFACT_PATH)
    private String artifactPath = StringUtils.EMPTY;

    @Column(DockerExecutorConfigDao.PUSH_IMAGE)
    private boolean pushImage = false;
    @Column(DockerExecutorConfigDao.PUSH_REGISTRY_TYPE)
    private RegistryType pushRegistryType;
    @Column(DockerExecutorConfigDao.DOCKERFILE)
    private String dockerfile;
    @Column(DockerExecutorConfigDao.DOCKER_CONTEXT)
    private String dockerContext;
    @Column(DockerExecutorConfigDao.DOCKER_REPO)
    private String dockerRepo;
    @Column(DockerExecutorConfigDao.DOCKER_IMAGE_NAME)
    private String dockerImageName;
    @Column(DockerExecutorConfigDao.DOCKER_VERSION_TYPE)
    private VersionType dockerVersionType;
    @Column(DockerExecutorConfigDao.DOCKER_TAG)
    private String dockerTag;
    @Column(DockerExecutorConfigDao.PLATFORM)
    private PlatformType platform;
    @Column(DockerExecutorConfigDao.ARCH)
    private ArchType arch;


}