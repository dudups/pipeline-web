package com.ezone.devops.plugins.job.build.maven.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.build.maven.file.bean.MavenBuildBean;
import com.ezone.devops.plugins.job.build.maven.file.bean.MavenConfigBean;
import com.ezone.devops.plugins.job.build.maven.file.model.MavenBuild;
import com.ezone.devops.plugins.job.build.maven.file.service.MavenBuildService;
import com.ezone.devops.plugins.job.build.maven.file.service.MavenConfigService;
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
public class MavenDataOperator implements PluginDataOperator<MavenConfigBean, MavenBuildBean> {

    @Autowired
    private MavenConfigService mavenConfigService;
    @Autowired
    private MavenBuildService mavenBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        MavenConfigBean mavenConfigBean = json.toJavaObject(MavenConfigBean.class);
        if (mavenConfigBean == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (mavenConfigBean.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.length(mavenConfigBean.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (mavenConfigBean.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (mavenConfigBean.isUploadArtifact()) {
            if (StringUtils.isBlank(mavenConfigBean.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(mavenConfigBean.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(mavenConfigBean.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (mavenConfigBean.isAutoGenerateConfig()) {
            if (StringUtils.isBlank(mavenConfigBean.getUserHomePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的秘钥生成路径不能为空");
            }
            if (StringUtils.equals(mavenConfigBean.getUserHomePath(), "/")) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "文件存储路径不能使用\"/\"");
            }
            if (CollectionUtils.isEmpty(mavenConfigBean.getPrivateRepoNames())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的仓库名称不能为空");
            }
        }

        if (mavenConfigBean.isEnableScan()) {
            if (StringUtils.isBlank(mavenConfigBean.getOutputPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描路径不能为空");
            }

            if (mavenConfigBean.getRulesetId() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描规则集不能为空");
            }

            if (mavenConfigBean.getScanLevel() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "扫描级别不能为空");
            }
        }

        if (mavenConfigBean.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(mavenConfigBean.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public MavenConfigBean getRealJob(Long id) {
        com.ezone.devops.plugins.job.build.maven.file.model.MavenConfig mavenConfig = mavenConfigService.getById(id);
        return JsonUtils.toObject(mavenConfig.getDataJson(), MavenConfigBean.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        MavenConfigBean mavenConfigBean = json.toJavaObject(MavenConfigBean.class);
        com.ezone.devops.plugins.job.build.maven.file.model.MavenConfig mavenConfig = new com.ezone.devops.plugins.job.build.maven.file.model.MavenConfig();
        mavenConfig.setDataJson(JsonUtils.toJson(mavenConfigBean));
        mavenConfigService.saveConfig(mavenConfig);
        return mavenConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return mavenConfigService.deleteById(id);
    }

    @Override
    public MavenBuildBean getRealJobRecord(Long id) {
        MavenBuild mavenBuild = mavenBuildService.getById(id);
        if (mavenBuild == null) {
            return null;
        }
        return JsonUtils.toObject(mavenBuild.getDataJson(), MavenBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        MavenConfigBean mavenConfigBean = getRealJob(realJobConfigId);
        MavenBuildBean mavenBuildBean = new MavenBuildBean();
        mavenBuildBean.setEnableScan(mavenConfigBean.isEnableScan());

        MavenBuild mavenBuild = new MavenBuild();
        mavenBuild.setDataJson(JsonUtils.toJson(mavenBuildBean));

        mavenBuildService.saveBuild(mavenBuild);
        return mavenBuild.getId();
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject buildJson) {
        MavenBuildBean mavenBuildBean = buildJson.toJavaObject(MavenBuildBean.class);
        MavenBuild mavenBuild = mavenBuildService.getById(realJobRecordId);
        mavenBuild.setId(realJobRecordId);
        mavenBuild.setDataJson(JsonUtils.toJson(mavenBuildBean));
        return mavenBuildService.updateBuild(mavenBuild);
    }
}
