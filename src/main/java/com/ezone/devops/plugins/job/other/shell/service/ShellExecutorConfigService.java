package com.ezone.devops.plugins.job.other.shell.service;

import com.ezone.devops.plugins.job.other.shell.model.ShellExecutorConfig;

public interface ShellExecutorConfigService {

    boolean deleteShellExecutorConfig(Long id);

    ShellExecutorConfig getById(Long id);

    ShellExecutorConfig saveShellExecutorConfig(ShellExecutorConfig shellExecutorConfig);
}
