package com.ezone.devops.plugins.job.build.cmake.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.cmake.file.bean.Cmake;
import com.ezone.devops.plugins.job.build.cmake.file.model.CmakeConfig;
import com.ezone.devops.plugins.job.build.cmake.file.service.CmakeConfigService;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CmakeDataOperator implements PluginDataOperator<Cmake, EmptyBuild> {

    @Autowired
    private CmakeConfigService cmakeConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        Cmake cmake = json.toJavaObject(Cmake.class);
        if (cmake == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (cmake.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(cmake.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (cmake.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (cmake.isUploadArtifact()) {
            if (StringUtils.isBlank(cmake.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(cmake.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(cmake.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (cmake.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(cmake.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public Cmake getRealJob(Long id) {
        CmakeConfig cmakeConfig = cmakeConfigService.getById(id);
        return JsonUtils.toObject(cmakeConfig.getDataJson(), Cmake.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        Cmake cmake = json.toJavaObject(Cmake.class);
        CmakeConfig cmakeConfig = new CmakeConfig();
        cmakeConfig.setDataJson(JsonUtils.toJson(cmake));
        cmakeConfigService.saveConfig(cmakeConfig);
        return cmakeConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return cmakeConfigService.deleteById(id);
    }

}
