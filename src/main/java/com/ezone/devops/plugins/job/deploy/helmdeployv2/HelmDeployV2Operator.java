package com.ezone.devops.plugins.job.deploy.helmdeployv2;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.clients.*;
import com.ezone.devops.pipeline.clients.request.InstallOrUpgradeRelease;
import com.ezone.devops.pipeline.clients.response.*;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.bean.HelmDeployBuildV2Bean;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.bean.HelmDeployConfigV2Bean;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ItsmCheckService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class HelmDeployV2Operator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private HelmDeployV2DataOperator helmDeployV2DataOperator;
    @Autowired
    private EzK8sV2Client ezK8sV2Client;
    @Autowired
    private ItsmCheckService itsmCheckService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        log.info("start execute job:[{}]", jobRecord);
        HelmDeployConfigV2Bean helmDeployConfigV2Bean = helmDeployV2DataOperator.getRealJob(jobRecord.getPluginId());
        HelmTemplateResponse helmTemplateResponse = ezK8sV2Client.getHelmTemplate(helmDeployConfigV2Bean, pipeline.getCompanyId());

        //添加itsm校验规则
        String errorMsg = itsmCheckService.isPass(repo,helmDeployConfigV2Bean.getClusterKey(),helmDeployConfigV2Bean.getNamespace(),jobRecord);
        if(!ObjectUtils.isEmpty(errorMsg) ){
            super.noticeJobFailed(pipelineRecord, jobRecord,errorMsg);
            return false;
        }

        if (helmTemplateResponse == null) {
            log.info("invoke ezk8s error,response is null job:[{}]", jobRecord);
            invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, "获取模板文件出错");
            return false;
        }

        if (helmTemplateResponse.isError()) {
            log.info("invoke ezk8s error response:[{}]", helmTemplateResponse);
            super.noticeJobFailed(pipelineRecord, jobRecord, helmTemplateResponse.getMessage());
            return false;
        }

        HelmTemplate helmTemplate = helmTemplateResponse.getData();
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
        String version;
        if (helmDeployConfigV2Bean.getVersionType() == VersionType.SNAPSHOT) {
            version = pipelineRecord.getSnapshotVersion();
        } else if (helmDeployConfigV2Bean.getVersionType() == VersionType.RELEASE) {
            String releaseVersion = pipelineRecord.getReleaseVersion();
            if (StringUtils.isEmpty(releaseVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                return false;
            }
            version = pipelineRecord.getReleaseVersion();
        } else {
            String customVersion = FreemarkerUtil.parseExpression(helmDeployConfigV2Bean.getCustomVersion(), envs);
            if (StringUtils.isEmpty(customVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "解析版本号出错");
                return false;
            }
            version = customVersion;
        }
        version = version.replaceAll("_", "-");

        String values = FreemarkerUtil.parseExpression(helmTemplate.getChartValues(), envs);
        InstallOrUpgradeRelease releaseInfo = new InstallOrUpgradeRelease();
        releaseInfo.setCompanyId(repo.getCompanyId());
        releaseInfo.setUsername(jobRecord.getTriggerUser());
        releaseInfo.setRepoName(helmTemplate.getRepoName());
        releaseInfo.setRepoType(helmTemplate.getRepoType());
        releaseInfo.setChartName(helmTemplate.getChartName());
        releaseInfo.setVersion(version);
        releaseInfo.setRelease(helmTemplate.getReleaseName());
        releaseInfo.setValues(values);
        releaseInfo.setAtomic(helmTemplate.isAtomic());
        releaseInfo.setWait(helmTemplate.isWait());
        releaseInfo.setTimeout(helmTemplate.getTimeout());

        releaseInfo.setCallbackUrl(getCallbackUrl(jobRecord));

        HelmDeployResponse response = ezK8sV2Client.createOrUpdateRelease(helmDeployConfigV2Bean.getClusterKey(), helmDeployConfigV2Bean.getNamespace(), releaseInfo);
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

        HelmDeployBuildV2Bean helmDeployBuildV2Bean = new HelmDeployBuildV2Bean();
        helmDeployBuildV2Bean.setClusterKey(helmDeployConfigV2Bean.getClusterKey());
        helmDeployBuildV2Bean.setNamespace(helmDeployConfigV2Bean.getNamespace());
        helmDeployBuildV2Bean.setChartName(helmTemplate.getChartName());
        helmDeployBuildV2Bean.setRelease(helmTemplate.getReleaseName());
        helmDeployBuildV2Bean.setVersion(version);
        helmDeployBuildV2Bean.setLogName(helmDeploy.getLogName());
        String jsonString = JSONObject.toJSONString(helmDeployBuildV2Bean);
        helmDeployV2DataOperator.updateRealJobRecord(jobRecord.getPluginRecordId(), JSONObject.parseObject(jsonString));
        return true;
    }
}