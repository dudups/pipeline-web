package com.ezone.devops.plugins.job.build.php.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.php.file.bean.Php;
import com.ezone.devops.plugins.job.build.php.file.model.PhpConfig;
import com.ezone.devops.plugins.job.build.php.file.service.PhpConfigService;
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
public class PhpDataOperator implements PluginDataOperator<Php, EmptyBuild> {

    @Autowired
    private PhpConfigService phpConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        Php php = json.toJavaObject(Php.class);
        if (php == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (php.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(php.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (php.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (php.isUploadArtifact()) {
            if (StringUtils.isBlank(php.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(php.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(php.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (php.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(php.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥文件生成路径不能为空");
            }
            if (StringUtils.equals(php.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(php.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
//            if (CollectionUtils.isEmpty(php.getPublicRepoNames())) {
//                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的镜像仓库名称不能为空");
//            }
        }

        if (php.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(php.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public Php getRealJob(Long id) {
        PhpConfig phpConfig = phpConfigService.getById(id);
        return JsonUtils.toObject(phpConfig.getDataJson(), Php.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        Php php = json.toJavaObject(Php.class);
        PhpConfig phpConfig = new PhpConfig();
        phpConfig.setDataJson(JsonUtils.toJson(php));
        phpConfigService.saveConfig(phpConfig);
        return phpConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return phpConfigService.deleteById(id);
    }

}
