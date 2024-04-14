package com.ezone.devops.plugins.job.build.custom.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.custom.file.bean.Custom;
import com.ezone.devops.plugins.job.build.custom.file.model.CustomConfig;
import com.ezone.devops.plugins.job.build.custom.file.service.CustomConfigService;
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
public class CustomDataOperator implements PluginDataOperator<Custom, EmptyBuild> {

    @Autowired
    private CustomConfigService customConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        Custom custom = json.toJavaObject(Custom.class);
        if (custom == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (custom.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(custom.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (custom.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (custom.isUploadArtifact()) {
            if (StringUtils.isBlank(custom.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(custom.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(custom.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (custom.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(custom.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public Custom getRealJob(Long id) {
        CustomConfig customConfig = customConfigService.getById(id);
        return JsonUtils.toObject(customConfig.getDataJson(), Custom.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        Custom custom = json.toJavaObject(Custom.class);
        CustomConfig customConfig = new CustomConfig();
        customConfig.setDataJson(JsonUtils.toJson(custom));
        customConfigService.saveConfig(customConfig);
        return customConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return customConfigService.deleteById(id);
    }

}
