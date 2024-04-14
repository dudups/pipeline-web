package com.ezone.devops.plugins.job.build.ant.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.build.ant.docker.bean.AntDockerBuildBean;
import com.ezone.devops.plugins.job.build.ant.docker.bean.AntDockerConfigBean;
import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerBuild;
import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerConfig;
import com.ezone.devops.plugins.job.build.ant.docker.service.AntDockerBuildService;
import com.ezone.devops.plugins.job.build.ant.docker.service.AntDockerConfigService;
import com.ezone.devops.plugins.job.enums.RegistryType;
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
public class AntDockerDataOperator implements PluginDataOperator<AntDockerConfigBean, AntDockerBuildBean> {

    @Autowired
    private AntDockerConfigService antDockerConfigService;
    @Autowired
    private AntDockerBuildService antDockerBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        AntDockerConfigBean antDockerConfigBean = json.toJavaObject(AntDockerConfigBean.class);
        if (antDockerConfigBean == null) {
            return false;
        }

        if (antDockerConfigBean.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(antDockerConfigBean.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(antDockerConfigBean.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (antDockerConfigBean.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(antDockerConfigBean.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(antDockerConfigBean.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(antDockerConfigBean.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(antDockerConfigBean.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (antDockerConfigBean.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (antDockerConfigBean.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (antDockerConfigBean.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(antDockerConfigBean.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (antDockerConfigBean.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (antDockerConfigBean.getDockerVersionType() == VersionType.DEFAULT_VERSION || antDockerConfigBean.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (antDockerConfigBean.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(antDockerConfigBean.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        if (antDockerConfigBean.isEnableScan()) {
            if (StringUtils.isBlank(antDockerConfigBean.getOutputPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描路径不能为空");
            }

            if (antDockerConfigBean.getRulesetId() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描规则集不能为空");
            }

            if (antDockerConfigBean.getScanLevel() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "扫描级别不能为空");
            }
        }

        return true;
    }

    @Override
    public AntDockerConfigBean getRealJob(Long id) {
        AntDockerConfig antDockerConfig = antDockerConfigService.getById(id);
        return JsonUtils.toObject(antDockerConfig.getDataJson(), AntDockerConfigBean.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        AntDockerConfigBean antDockerConfigBean = json.toJavaObject(AntDockerConfigBean.class);
        AntDockerConfig antDockerConfig = new AntDockerConfig();
        antDockerConfig.setDataJson(JsonUtils.toJson(antDockerConfigBean));
        antDockerConfigService.saveConfig(antDockerConfig);
        return antDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return antDockerConfigService.deleteById(id);
    }

    @Override
    public AntDockerBuildBean getRealJobRecord(Long id) {
        AntDockerBuild antDockerBuild = antDockerBuildService.getById(id);
        if (antDockerBuild == null) {
            return null;
        }

        return JsonUtils.toObject(antDockerBuild.getDataJson(), AntDockerBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        AntDockerConfigBean antDockerConfigBean = getRealJob(realJobConfigId);
        AntDockerBuildBean antDockerBuildBean = new AntDockerBuildBean();
        antDockerBuildBean.setEnableScan(antDockerConfigBean.isEnableScan());

        AntDockerBuild antDockerBuild = new AntDockerBuild();
        antDockerBuild.setDataJson(JsonUtils.toJson(antDockerBuildBean));
        antDockerBuildService.saveBuild(antDockerBuild);
        return antDockerBuild.getId();
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject buildJson) {
        AntDockerBuildBean antDockerBuildBean = buildJson.toJavaObject(AntDockerBuildBean.class);
        AntDockerBuild antDockerBuild = antDockerBuildService.getById(realJobRecordId);
        antDockerBuild.setId(realJobRecordId);
        antDockerBuild.setDataJson(JsonUtils.toJson(antDockerBuildBean));
        return antDockerBuildService.updateBuild(antDockerBuild);
    }
}
