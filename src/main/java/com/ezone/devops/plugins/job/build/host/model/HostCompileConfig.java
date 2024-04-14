package com.ezone.devops.plugins.job.build.host.model;

import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.build.host.dao.HostCompileConfigDao;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Table(name = "plugin_host_compile_config")
public class HostCompileConfig extends LongID {

    @Column(HostCompileConfigDao.ID)
    private Long id;
    @Column(HostCompileConfigDao.CLONE_MODE)
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    @Column(HostCompileConfigDao.BUILD_COMMAND)
    private String buildCommand;

    @Column(HostCompileConfigDao.UPLOAD_ARTIFACT)
    private boolean uploadArtifact = true;
    @Column(HostCompileConfigDao.PKG_REPO)
    private String pkgRepo = StringUtils.EMPTY;
    @Column(HostCompileConfigDao.PKG_NAME)
    private String pkgName = StringUtils.EMPTY;
    @Column(HostCompileConfigDao.ARTIFACT_PATH)
    private String artifactPath = StringUtils.EMPTY;

    @Column(HostCompileConfigDao.UPLOAD_REPORT)
    private boolean uploadReport = false;
    @Column(HostCompileConfigDao.REPORT_DIR)
    private String reportDir = StringUtils.EMPTY;
    @Column(HostCompileConfigDao.REPORT_INDEX)
    private String reportIndex = StringUtils.EMPTY;

    // 是否使用自己配置资源池
    @Column(HostCompileConfigDao.USE_SELF_CI_POOL)
    private boolean useSelfCiPool = false;
    @Column(HostCompileConfigDao.CLUSTER_NAME)
    private String clusterName = StringUtils.EMPTY;

    @Column(HostCompileConfigDao.VERSION_TYPE)
    private VersionType versionType = VersionType.SNAPSHOT;
    @Column(HostCompileConfigDao.CUSTOM_VERSION)
    private String customVersion = StringUtils.EMPTY;
}