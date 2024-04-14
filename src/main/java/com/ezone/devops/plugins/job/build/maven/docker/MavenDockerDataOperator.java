package com.ezone.devops.plugins.job.build.maven.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.build.maven.docker.bean.MavenDockerBuildBean;
import com.ezone.devops.plugins.job.build.maven.docker.bean.MavenDockerConfigBean;
import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerBuild;
import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerConfig;
import com.ezone.devops.plugins.job.build.maven.docker.service.MavenDockerBuildService;
import com.ezone.devops.plugins.job.build.maven.docker.service.MavenDockerConfigService;
import com.ezone.devops.plugins.job.enums.RegistryType;
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
public class MavenDockerDataOperator implements PluginDataOperator<MavenDockerConfigBean, MavenDockerBuildBean> {

    @Autowired
    private MavenDockerConfigService mavenDockerConfigService;
    @Autowired
    private MavenDockerBuildService mavenDockerBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        MavenDockerConfigBean mavenDockerConfigBean = json.toJavaObject(MavenDockerConfigBean.class);
        if (mavenDockerConfigBean == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (mavenDockerConfigBean.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(mavenDockerConfigBean.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (StringUtils.isBlank(mavenDockerConfigBean.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不允许为空");
        }

        if (mavenDockerConfigBean.getPushRegistryType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库类型不允许为空");
        }

        if (StringUtils.isBlank(mavenDockerConfigBean.getDockerContext())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker上下文不允许为空");
        }

        if (StringUtils.isBlank(mavenDockerConfigBean.getDockerfile())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的dockerfile不允许为空");
        }

        if (StringUtils.isBlank(mavenDockerConfigBean.getDockerRepo())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker制品库不允许为空");
        }

        if (StringUtils.isBlank(mavenDockerConfigBean.getDockerImageName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker镜像名称不允许为空");
        }

        if (mavenDockerConfigBean.getPushRegistryType() == RegistryType.EXTERNAL) {
            if (mavenDockerConfigBean.getDockerVersionType() == VersionType.DEFAULT_VERSION) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (mavenDockerConfigBean.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(mavenDockerConfigBean.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else if (mavenDockerConfigBean.getPushRegistryType() == RegistryType.INTERNAL_PKG) {
            if (mavenDockerConfigBean.getDockerVersionType() == VersionType.DEFAULT_VERSION || mavenDockerConfigBean.getDockerVersionType() == VersionType.RELEASE) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "不支持的版本类型");
            } else if (mavenDockerConfigBean.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
                if (StringUtils.isBlank(mavenDockerConfigBean.getDockerTag())) {
                    throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的docker tag不允许为空");
                }
            }
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "不支持的docker制品库类型");
        }

        if (mavenDockerConfigBean.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(mavenDockerConfigBean.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的setting的生成路径不能为空");
            }
            if (StringUtils.equals(mavenDockerConfigBean.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(mavenDockerConfigBean.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
        }

        if (mavenDockerConfigBean.isEnableScan()) {
            if (StringUtils.isBlank(mavenDockerConfigBean.getOutputPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描路径不能为空");
            }

            if (mavenDockerConfigBean.getRulesetId() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描规则集不能为空");
            }

            if (mavenDockerConfigBean.getScanLevel() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "扫描级别不能为空");
            }
        }

        return true;
    }

    @Override
    public MavenDockerConfigBean getRealJob(Long id) {
        MavenDockerConfig mavenDockerConfig = mavenDockerConfigService.getById(id);
        return JsonUtils.toObject(mavenDockerConfig.getDataJson(), MavenDockerConfigBean.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        MavenDockerConfigBean mavenDockerConfigBean = json.toJavaObject(MavenDockerConfigBean.class);
        MavenDockerConfig mavenDockerConfig = new MavenDockerConfig();
        mavenDockerConfig.setDataJson(JsonUtils.toJson(mavenDockerConfigBean));
        mavenDockerConfigService.saveConfig(mavenDockerConfig);
        return mavenDockerConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return mavenDockerConfigService.deleteById(id);
    }

    @Override
    public MavenDockerBuildBean getRealJobRecord(Long id) {
        MavenDockerBuild mavenDockerBuild = mavenDockerBuildService.getById(id);
        if (mavenDockerBuild == null) {
            return null;
        }
        return JsonUtils.toObject(mavenDockerBuild.getDataJson(), MavenDockerBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        MavenDockerConfigBean mavenDockerConfigBean = getRealJob(realJobConfigId);
        MavenDockerBuildBean mavenDockerBuildBean = new MavenDockerBuildBean();
        mavenDockerBuildBean.setEnableScan(mavenDockerConfigBean.isEnableScan());

        MavenDockerBuild mavenDockerBuild = new MavenDockerBuild();
        mavenDockerBuild.setDataJson(JsonUtils.toJson(mavenDockerBuildBean));

        mavenDockerBuildService.saveBuild(mavenDockerBuild);
        return mavenDockerBuild.getId();
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject buildJson) {
        MavenDockerBuildBean mavenDockerBuildBean = buildJson.toJavaObject(MavenDockerBuildBean.class);
        MavenDockerBuild mavenDockerBuild = mavenDockerBuildService.getById(realJobRecordId);
        mavenDockerBuild.setId(realJobRecordId);
        mavenDockerBuild.setDataJson(JsonUtils.toJson(mavenDockerBuildBean));
        return mavenDockerBuildService.updateBuild(mavenDockerBuild);
    }
}
