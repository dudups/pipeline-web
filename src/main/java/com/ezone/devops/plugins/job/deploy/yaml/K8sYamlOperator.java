package com.ezone.devops.plugins.job.deploy.yaml;

import com.ezone.devops.ezcode.sdk.bean.model.InternalFile;
import com.ezone.devops.ezcode.sdk.service.InternalFileService;
import com.ezone.devops.pipeline.clients.EzK8sV2Client;
import com.ezone.devops.pipeline.clients.request.YamlPayload;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.deploy.yaml.bean.K8sYamlConfigBean;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class K8sYamlOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private K8sYamlDataOperator k8sYamlDataOperator;
    @Autowired
    private EzK8sV2Client ezK8sV2Client;
    @Autowired
    private InternalFileService internalFileService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        log.info("start execute job:[{}]", jobRecord);
        K8sYamlConfigBean k8SYamlConfigBean = k8sYamlDataOperator.getRealJob(jobRecord.getPluginId());

        YamlPayload payload = new YamlPayload();
        payload.setCompanyId(repo.getCompanyId());
        payload.setUsername(jobRecord.getTriggerUser());

        String yaml;
        if (k8SYamlConfigBean.isUseRepoFile()) {
            String filePath = k8SYamlConfigBean.getRepoFilePath();
            InternalFile internalFile = internalFileService.getFile(pipeline.getCompanyId(), pipeline.getRepoKey(), pipelineRecord.getCommitId(), filePath);
            if (internalFile == null) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "文件" + filePath + "在代码库中不存在");
                return false;
            }

            if (internalFile.isSuperLarge()) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "文件" + filePath + "太大，无法读取");
                return false;
            }

            if (internalFile.isBinary()) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "文件" + filePath + "是二进制文件，不支持部署");
                return false;
            }

            yaml = new String(internalFile.getContent());
        } else {
            yaml = k8SYamlConfigBean.getYaml();
        }

        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
        yaml = FreemarkerUtil.parseExpression(yaml, envs);
        if (StringUtils.isEmpty(yaml)) {
            super.noticeJobFailed(pipelineRecord, jobRecord, "解析yaml内容失败");
            return false;
        }
        payload.setYaml(yaml);

        BaseResponse<?> response = ezK8sV2Client.deployYaml(k8SYamlConfigBean.getClusterKey(), payload);
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

        super.noticeJobSuccess(pipelineRecord, jobRecord, response.getMessage());
        return true;
    }
}