package com.ezone.devops.plugins.job.build.host.service;

import com.ezone.devops.plugins.job.build.host.model.HostCompileConfig;

import java.util.Collection;
import java.util.List;

public interface HostCompileConfigService {

    HostCompileConfig getById(Long id);

    List<HostCompileConfig> findByIds(Collection<Long> ids);

    boolean deleteById(Long id);

    void saveConfig(HostCompileConfig hostCompileConfig);

}
