package com.ezone.devops.plugins.job.build.python.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.bean.EmptyBuild;
import com.ezone.devops.plugins.job.build.python.file.bean.Python;
import com.ezone.devops.plugins.job.build.python.file.model.PythonConfig;
import com.ezone.devops.plugins.job.build.python.file.service.PythonConfigService;
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
public class PythonDataOperator implements PluginDataOperator<Python, EmptyBuild> {

    @Autowired
    private PythonConfigService pythonConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        Python python = json.toJavaObject(Python.class);
        if (python == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (python.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(python.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (python.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (python.isUploadArtifact()) {
            if (StringUtils.isBlank(python.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(python.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(python.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (python.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(python.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥文件生成路径不能为空");
            }
            if (StringUtils.equals(python.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(python.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
//            if (CollectionUtils.isEmpty(python.getPublicRepoNames())) {
//                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的镜像仓库名称不能为空");
//            }
        }

        if (python.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(python.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public Python getRealJob(Long id) {
        PythonConfig pythonConfig = pythonConfigService.getById(id);
        return JsonUtils.toObject(pythonConfig.getDataJson(), Python.class);
    }


    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        Python python = json.toJavaObject(Python.class);
        PythonConfig pythonConfig = new PythonConfig();
        pythonConfig.setDataJson(JsonUtils.toJson(python));
        pythonConfigService.saveConfig(pythonConfig);
        return pythonConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return pythonConfigService.deleteById(id);
    }

}
