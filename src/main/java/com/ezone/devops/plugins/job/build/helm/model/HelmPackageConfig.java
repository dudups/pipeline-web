package com.ezone.devops.plugins.job.build.helm.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.ArtifactRepoType;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.build.helm.dao.HelmPackageConfigDao;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Table(name = "plugin_helm_package_config")
public class HelmPackageConfig extends LongID {

    @Column(HelmPackageConfigDao.ID)
    private Long id;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(HelmPackageConfigDao.BUILD_IMAGE_ID)
    private Long buildImageId;
    @Column(HelmPackageConfigDao.CLONE_MODE)
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    @Column(HelmPackageConfigDao.COMMAND)
    private String command = StringUtils.EMPTY;

    @Column(HelmPackageConfigDao.USE_DEFAULT_NAME)
    private boolean useDefaultName;
    @Column(HelmPackageConfigDao.CHART_NAME)
    private String chartName = StringUtils.EMPTY;

    @Column(HelmPackageConfigDao.PKG_REPO_TYPE)
    private ArtifactRepoType pkgRepoType;
    @Column(HelmPackageConfigDao.PKG_REPO_NAME)
    private String pkgRepoName;

    @Column(HelmPackageConfigDao.CHART_RESOURCE_PATH)
    private String chartResourcePath = StringUtils.EMPTY;

    @Column(HelmPackageConfigDao.CHART_VERSION_TYPE)
    private VersionType chartVersionType;
    @Column(HelmPackageConfigDao.CUSTOM_VERSION)
    private String customVersion = StringUtils.EMPTY;

    @Column(HelmPackageConfigDao.APP_VERSION_TYPE)
    private VersionType appVersionType;
    @Column(HelmPackageConfigDao.APP_CUSTOM_VERSION)
    private String appCustomVersion = StringUtils.EMPTY;

}