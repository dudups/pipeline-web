package com.ezone.devops.plugins.job.build.ant.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.build.ant.file.bean.AntBuildBean;
import com.ezone.devops.plugins.job.build.ant.file.bean.AntConfigBean;
import com.ezone.devops.plugins.job.build.ant.file.model.AntBuild;
import com.ezone.devops.plugins.job.build.ant.file.model.AntConfig;
import com.ezone.devops.plugins.job.build.ant.file.service.AntBuildService;
import com.ezone.devops.plugins.job.build.ant.file.service.AntConfigService;
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
public class AntDataOperator implements PluginDataOperator<AntConfigBean, AntBuildBean> {

    @Autowired
    private AntConfigService antConfigService;
    @Autowired
    private AntBuildService antBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        AntConfigBean antConfigBean = json.toJavaObject(AntConfigBean.class);
        if (antConfigBean == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "解析" + job.getName() + "插件数据结构出错");
        }

        if (StringUtils.length(antConfigBean.getCommand()) > 30000) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建命令不得超过30000个字符");
        }

        if (antConfigBean.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (antConfigBean.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (antConfigBean.isUploadArtifact()) {
            if (StringUtils.isBlank(antConfigBean.getPkgRepo())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品库不允许为空");
            }

            if (StringUtils.isBlank(antConfigBean.getPkgName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品名称不允许为空");
            }

            if (StringUtils.isBlank(antConfigBean.getArtifactPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的制品路径不允许为空");
            }
        }

        if (antConfigBean.isEnableScan()) {
            if (StringUtils.isBlank(antConfigBean.getOutputPath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描路径不能为空");
            }

            if (antConfigBean.getRulesetId() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的扫描规则集不能为空");
            }

            if (antConfigBean.getScanLevel() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "扫描级别不能为空");
            }
        }

        if (antConfigBean.getVersionType() == VersionType.CUSTOM_VERSION) {
            if (StringUtils.isBlank(antConfigBean.getCustomVersion())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的自定义版本不允许为空");
            }
        }

        return true;
    }

    @Override
    public AntConfigBean getRealJob(Long id) {
        AntConfig antConfig = antConfigService.getById(id);
        return JsonUtils.toObject(antConfig.getDataJson(), AntConfigBean.class);
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        AntConfigBean antConfigBean = json.toJavaObject(AntConfigBean.class);
        AntConfig antConfig = new AntConfig();
        antConfig.setDataJson(JsonUtils.toJson(antConfigBean));
        antConfigService.saveConfig(antConfig);
        return antConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return antConfigService.deleteById(id);
    }

    @Override
    public AntBuildBean getRealJobRecord(Long id) {
        AntBuild antBuild = antBuildService.getById(id);
        if (antBuild == null) {
            return null;
        }

        return JsonUtils.toObject(antBuild.getDataJson(), AntBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        AntConfigBean antConfigBean = getRealJob(realJobConfigId);
        AntBuildBean antBuildBean = new AntBuildBean();
        antBuildBean.setEnableScan(antConfigBean.isEnableScan());

        AntBuild antBuild = new AntBuild();
        antBuild.setDataJson(JsonUtils.toJson(antBuildBean));

        antBuildService.saveBuild(antBuild);
        return antBuild.getId();
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject buildJson) {
        AntBuildBean antBuildBean = buildJson.toJavaObject(AntBuildBean.class);
        AntBuild antBuild = antBuildService.getById(realJobRecordId);
        antBuild.setId(realJobRecordId);
        antBuild.setDataJson(JsonUtils.toJson(antBuildBean));
        return antBuildService.updateBuild(antBuild);
    }
}
