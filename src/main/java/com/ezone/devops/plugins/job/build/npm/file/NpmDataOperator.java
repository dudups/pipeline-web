package com.ezone.devops.plugins.job.build.npm.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.npm.file.bean.Npm;
import com.ezone.devops.plugins.job.build.npm.file.model.NpmConfig;
import com.ezone.devops.plugins.job.build.npm.file.service.NpmConfigService;
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
public class NpmDataOperator implements PluginDataOperator<Npm, EmptyBuild> {

    @Autowired
    private NpmConfigService npmConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        Npm npm = json.toJavaObject(Npm.class);
        if (npm == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (npm.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(npm.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (npm.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (npm.isUploadArtifact()) {
            if (StringUtils.isBlank(npm.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(npm.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(npm.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (npm.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(npm.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥文件生成路径不能为空");
            }
            if (StringUtils.equals(npm.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(npm.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
//            if (CollectionUtils.isEmpty(npm.getPublicRepoNames())) {
//                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的镜像仓库名称不能为空");
//            }
        }

        if (npm.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(npm.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public Npm getRealJob(Long id) {
        NpmConfig npmConfig = npmConfigService.getById(id);
        return JsonUtils.toObject(npmConfig.getDataJson(), Npm.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        Npm npm = json.toJavaObject(Npm.class);
        NpmConfig npmConfig = new NpmConfig();
        npmConfig.setDataJson(JsonUtils.toJson(npm));
        npmConfigService.saveConfig(npmConfig);
        return npmConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return npmConfigService.deleteById(id);
    }

}
