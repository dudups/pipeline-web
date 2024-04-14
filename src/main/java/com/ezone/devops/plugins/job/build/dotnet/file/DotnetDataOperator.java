package com.ezone.devops.plugins.job.build.dotnet.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.dotnet.file.bean.Dotnet;
import com.ezone.devops.plugins.job.build.dotnet.file.model.DotnetConfig;
import com.ezone.devops.plugins.job.build.dotnet.file.service.DotnetConfigService;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DotnetDataOperator implements PluginDataOperator<Dotnet, EmptyBuild> {

    @Autowired
    private DotnetConfigService dotnetConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        Dotnet dotnet = json.toJavaObject(Dotnet.class);
        if (dotnet == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (dotnet.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(dotnet.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (dotnet.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (dotnet.isUploadArtifact()) {
            if (StringUtils.isBlank(dotnet.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(dotnet.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(dotnet.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (dotnet.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(dotnet.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥生成路径不能为空");
            }
            if (StringUtils.equals(dotnet.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(dotnet.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
//            if (CollectionUtils.isEmpty(dotnet.getPublicRepoNames())) {
//                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的镜像仓库名称不能为空");
//            }
        }

        if (dotnet.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(dotnet.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public Dotnet getRealJob(Long id) {
        DotnetConfig dotnetConfig = dotnetConfigService.getById(id);
        return JsonUtils.toObject(dotnetConfig.getDataJson(), Dotnet.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        Dotnet dotnet = json.toJavaObject(Dotnet.class);
        DotnetConfig dotnetConfig = new DotnetConfig();
        dotnetConfig.setDataJson(JsonUtils.toJson(dotnet));
        dotnetConfigService.saveConfig(dotnetConfig);
        return dotnetConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return dotnetConfigService.deleteById(id);
    }

}
