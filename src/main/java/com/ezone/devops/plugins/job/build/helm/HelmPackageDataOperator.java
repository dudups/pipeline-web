package com.ezone.devops.plugins.job.build.helm;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.build.helm.model.HelmPackageBuild;
import com.ezone.devops.plugins.job.build.helm.model.HelmPackageConfig;
import com.ezone.devops.plugins.job.build.helm.service.HelmPackageBuildService;
import com.ezone.devops.plugins.job.build.helm.service.HelmPackageConfigService;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class HelmPackageDataOperator implements PluginDataOperator<HelmPackageConfig, HelmPackageBuild> {

    private static final Pattern NAME_REGEXP = Pattern.compile("^[a-z0-9\\-]+$");

    @Autowired
    private HelmPackageBuildService helmPackageBuildService;
    @Autowired
    private HelmPackageConfigService helmPackageConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        HelmPackageConfig helmPackageConfig = json.toJavaObject(HelmPackageConfig.class);
        if (helmPackageConfig == null) {
            return false;
        }

        if (helmPackageConfig.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(helmPackageConfig.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (helmPackageConfig.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请选择环境版本");
        }

        if (helmPackageConfig.getPkgRepoType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请输入制品库类型");
        }

        if (!helmPackageConfig.isUseDefaultName()) {
            Matcher matcher = NAME_REGEXP.matcher(helmPackageConfig.getChartName());
            if (!matcher.matches()) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "chart名称只能是小写字母、数字以及\"-\"");
            }
            if (StringUtils.isBlank(helmPackageConfig.getChartName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请输入的chart名称");
            }
        }

        if (StringUtils.isBlank(helmPackageConfig.getPkgRepoName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "其输入制品库名称");
        }

        if (StringUtils.isBlank(helmPackageConfig.getChartResourcePath())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请输入chart包资源的路径");
        } else {
            if (StringUtils.startsWith(helmPackageConfig.getChartResourcePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请输入相对于代码库的chart文件所在的目录，不要以\"/\"开头");
            }
        }

        if (StringUtils.isBlank(helmPackageConfig.getChartResourcePath())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请输入chart包资源的路径");
        }


        VersionType chartVersionType = helmPackageConfig.getChartVersionType();
        if (chartVersionType == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "自定义的chart版本不能为空");
        } else {
            if (chartVersionType == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(helmPackageConfig.getCustomVersion())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请输入自定义的chart版本");
                }
            }
        }

        VersionType appVersionType = helmPackageConfig.getAppVersionType();
        if (appVersionType == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "自定义的app版本不能为空");
        } else {
            if (appVersionType == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(helmPackageConfig.getAppCustomVersion())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请输入自定义的app版本");
                }
            }

        }
        return true;
    }


    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        HelmPackageConfig helmPackageConfig = json.toJavaObject(HelmPackageConfig.class);
        helmPackageConfig = helmPackageConfigService.saveConfig(helmPackageConfig);
        return helmPackageConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return helmPackageConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        HelmPackageBuild helmPackageBuild = json.toJavaObject(HelmPackageBuild.class);
        return helmPackageBuildService.updateBuild(helmPackageBuild);
    }

    @Override
    public HelmPackageConfig getRealJob(Long id) {
        return helmPackageConfigService.getById(id);
    }

    @Override
    public HelmPackageBuild getRealJobRecord(Long id) {
        return helmPackageBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        HelmPackageConfig helmPackageConfig = getRealJob(realJobConfigId);
        if (null == helmPackageConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        HelmPackageBuild helmPackageBuild = new HelmPackageBuild();
        helmPackageBuild = helmPackageBuildService.saveBuild(helmPackageBuild);
        log.debug("save build:[{}]", helmPackageBuild);
        return helmPackageBuild.getId();
    }
}
