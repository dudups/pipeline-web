package com.ezone.devops.plugins.job.other.docker.dao;

import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface DockerExecutorConfigDao extends LongKeyBaseDao<DockerExecutorConfig> {

    String ID = "id";
    String CLONE_MODE = "clone_mode";
    String PROVIDER_NAME = "provider_name";
    String REGISTRY_ID = "registry_id";
    String REGISTRY_TYPE = "registry_type";
    String IMAGE_NAME = "image_name";
    String VERSION_TYPE = "version_type";
    String VERSION = "version";
    String COMMAND = "command";

    String UPLOAD_REPORT = "upload_report";
    String REPORT_DIR = "report_dir";
    String REPORT_INDEX = "report_index";

    String UPLOAD_ARTIFACT = "upload_artifact";
    String PKG_REPO = "pkg_repo";
    String PKG_NAME = "pkg_name";
    String ARTIFACT_PATH = "artifact_path";

    String PUSH_IMAGE = "push_image";
    String PUSH_REGISTRY_TYPE = "push_registry_type";
    String DOCKERFILE = "dockerfile";
    String DOCKER_CONTEXT = "docker_context";
    String DOCKER_REPO = "docker_repo";
    String DOCKER_IMAGE_NAME = "docker_image_name";
    String DOCKER_VERSION_TYPE = "docker_version_type";
    String DOCKER_TAG = "docker_tag";
    String PLATFORM = "platform";
    String ARCH = "arch";

}
