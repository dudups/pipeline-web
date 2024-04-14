package com.ezone.devops.plugins.job.other.shell.dao;

import com.ezone.devops.plugins.job.other.shell.model.ShellExecutorConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface ShellExecutorConfigDao extends LongKeyBaseDao<ShellExecutorConfig> {

    String ID = "id";
    String CLONE_MODE = "clone_mode";
    String BUILD_IMAGE_ID = "build_image_id";
    String COMMAND = "command";
}
