package com.ezone.devops.plugins.job.deploy.helmdeploy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.clients.EzK8sV2Client;
import com.ezone.devops.pipeline.clients.EzProjectClient;
import com.ezone.devops.pipeline.clients.request.EzProjectPayload;
import com.ezone.devops.pipeline.clients.request.InstallOrUpgradeRelease;
import com.ezone.devops.pipeline.clients.response.EzK8sWebClientResponse;
import com.ezone.devops.pipeline.clients.response.HelmDeploy;
import com.ezone.devops.pipeline.clients.response.HelmDeployResponse;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.util.SystemConstant;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.deploy.helmdeploy.bean.HelmDeployBuildBean;
import com.ezone.devops.plugins.job.deploy.helmdeploy.bean.HelmDeployConfigBean;
import com.ezone.devops.plugins.job.enums.RepoType;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ItsmCheckService;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HelmDeployOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private HelmDeployDataOperator helmDeployDataOperator;
    @Autowired
    private EzK8sV2Client ezK8sV2Client;
    @Autowired
    private ItsmCheckService itsmCheckService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        log.info("start execute job:[{}]", jobRecord);
        HelmDeployConfigBean helmDeployConfigBean = helmDeployDataOperator.getRealJob(jobRecord.getPluginId());
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        //添加itsm校验规则
        String errorMsg = itsmCheckService.isPass(repo,helmDeployConfigBean.getClusterKey(),helmDeployConfigBean.getNamespace(),jobRecord);
        if(!ObjectUtils.isEmpty(errorMsg)){
            super.noticeJobFailed(pipelineRecord, jobRecord,errorMsg);
            return false;
        }

        String version;
        if (helmDeployConfigBean.getVersionType() == VersionType.SNAPSHOT) {
            version = pipelineRecord.getSnapshotVersion();
        } else if (helmDeployConfigBean.getVersionType() == VersionType.RELEASE) {
            String releaseVersion = pipelineRecord.getReleaseVersion();
            if (StringUtils.isEmpty(releaseVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                return false;
            }
            version = pipelineRecord.getReleaseVersion();
        } else {
            String customVersion = FreemarkerUtil.parseExpression(helmDeployConfigBean.getCustomVersion(), envs);
            if (StringUtils.isEmpty(customVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "解析版本号出错");
                return false;
            }
            version = customVersion;
        }
        version = version.replaceAll("_", "-");

        InstallOrUpgradeRelease releaseInfo = new InstallOrUpgradeRelease();
        releaseInfo.setCompanyId(repo.getCompanyId());
        releaseInfo.setUsername(jobRecord.getTriggerUser());
        releaseInfo.setRepoName(helmDeployConfigBean.getRepoName());

        String repoTypeStr = helmDeployConfigBean.getRepoType();
        RepoType repoType;
        if (StringUtils.equals(repoTypeStr.toLowerCase(), "snapshot")) {
            repoType = RepoType.SNAPSHOT;
        } else {
            repoType = RepoType.RELEASE;
        }


        String values = FreemarkerUtil.parseExpression(helmDeployConfigBean.getChartValues(), envs);
        releaseInfo.setRepoType(repoType);
        releaseInfo.setChartName(helmDeployConfigBean.getChartName());
        releaseInfo.setVersion(version);
        releaseInfo.setRelease(helmDeployConfigBean.getReleaseName());
        releaseInfo.setValues(values);
        releaseInfo.setWait(helmDeployConfigBean.isWait());
        releaseInfo.setAtomic(helmDeployConfigBean.isAtomic());
        releaseInfo.setTimeout(helmDeployConfigBean.getTimeout());
        releaseInfo.setCallbackUrl(getCallbackUrl(jobRecord));

        HelmDeployResponse response = ezK8sV2Client.createOrUpdateRelease(helmDeployConfigBean.getClusterKey(), helmDeployConfigBean.getNamespace(), releaseInfo);
        if (response == null) {
            log.info("invoke ezk8s error,response is null job:[{}]", jobRecord);
            invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, ezK8sV2Client.getPlatformName());
            return false;
        }

        if (response.isError()) {
            log.info("invoke ezk8s error response:[{}]", response);
            super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
            return false;
        }

        HelmDeploy helmDeploy = response.getData();

        HelmDeployBuildBean helmDeployBuildBean = new HelmDeployBuildBean();
        helmDeployBuildBean.setClusterKey(helmDeployConfigBean.getClusterKey());
        helmDeployBuildBean.setNamespace(helmDeployConfigBean.getNamespace());
        helmDeployBuildBean.setChartName(helmDeployConfigBean.getChartName());
        helmDeployBuildBean.setRelease(releaseInfo.getRelease());
        helmDeployBuildBean.setLogName(helmDeploy.getLogName());
        helmDeployBuildBean.setVersion(version);
        String jsonString = JSONObject.toJSONString(helmDeployBuildBean);
        helmDeployDataOperator.updateRealJobRecord(jobRecord.getPluginRecordId(), JSONObject.parseObject(jsonString));
        return true;
    }
}