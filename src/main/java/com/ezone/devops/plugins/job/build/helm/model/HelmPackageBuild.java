package com.ezone.devops.plugins.job.build.helm.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.plugins.job.build.helm.dao.HelmPackageBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_helm_package_build")
public class HelmPackageBuild extends LongID {

    @JSONField(serialize = false)
    @Column(HelmPackageBuildDao.ID)
    private Long id;

}