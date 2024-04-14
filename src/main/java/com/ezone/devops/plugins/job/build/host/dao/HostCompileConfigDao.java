package com.ezone.devops.plugins.job.build.host.dao;

import com.ezone.devops.plugins.job.build.host.model.HostCompileConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HostCompileConfigDao extends LongKeyBaseDao<HostCompileConfig> {

    String ID = "id";
    String CLONE_MODE = "clone_mode";
    String BUILD_COMMAND = "build_command";
    String UPLOAD_ARTIFACT = "upload_artifact";
    String PKG_REPO = "pkg_repo";
    String PKG_NAME = "pkg_name";
    String ARTIFACT_PATH = "artifact_path";
    String USE_SELF_CI_POOL = "use_self_ci_pool";
    String CLUSTER_NAME = "cluster_name";
    String VERSION_TYPE = "version_type";
    String CUSTOM_VERSION = "custom_version";

    String UPLOAD_REPORT = "upload_report";
    String REPORT_DIR = "report_dir";
    String REPORT_INDEX = "report_index";

}
