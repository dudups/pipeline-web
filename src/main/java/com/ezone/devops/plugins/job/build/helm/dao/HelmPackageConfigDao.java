package com.ezone.devops.plugins.job.build.helm.dao;

import com.ezone.devops.plugins.job.build.helm.model.HelmPackageConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HelmPackageConfigDao extends LongKeyBaseDao<HelmPackageConfig> {

    String ID = "id";
    String BUILD_IMAGE_ID = "build_image_id";
    String CLONE_MODE = "clone_mode";
    String CHART_RESOURCE_PATH = "chart_resource_path";
    String COMMAND = "command";
    String USE_DEFAULT_NAME = "use_default_name";
    String CHART_NAME = "chart_name";
    String PKG_REPO_TYPE = "pkg_repo_type";
    String PKG_REPO_NAME = "pkg_repo_name";
    String CHART_VERSION_TYPE = "chart_version_type";
    String CUSTOM_VERSION = "custom_version";
    String APP_VERSION_TYPE = "app_version_type";
    String APP_CUSTOM_VERSION = "app_custom_version";
}
