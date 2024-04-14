package com.ezone.devops.plugins.job.build.go.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.go.file.bean.Go;
import com.ezone.devops.plugins.job.build.go.file.model.GoConfig;
import com.ezone.devops.plugins.job.build.go.file.service.GoConfigService;
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
public class GoDataOperator implements PluginDataOperator<Go, EmptyBuild> {

    @Autowired
    private GoConfigService goConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        Go go = json.toJavaObject(Go.class);
        if (go == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (go.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(go.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (go.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (go.isUploadArtifact()) {
            if (StringUtils.isBlank(go.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(go.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(go.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (go.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(go.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public Go getRealJob(Long id) {
        GoConfig goConfig = goConfigService.getById(id);
        return JsonUtils.toObject(goConfig.getDataJson(), Go.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        Go go = json.toJavaObject(Go.class);
        GoConfig goConfig = new GoConfig();
        goConfig.setDataJson(JsonUtils.toJson(go));
        goConfigService.saveConfig(goConfig);
        return goConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return goConfigService.deleteById(id);
    }

}
