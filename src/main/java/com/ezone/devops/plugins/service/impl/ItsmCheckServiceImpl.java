package com.ezone.devops.plugins.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.clients.EzK8sV2Client;
import com.ezone.devops.pipeline.clients.EzProjectClient;;
import com.ezone.devops.pipeline.clients.request.EzProjectPayload;;
import com.ezone.devops.pipeline.clients.response.EzK8sWebClientResponse;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.util.SystemConstant;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.service.ItsmCheckService;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ItsmCheckServiceImpl implements ItsmCheckService {

    @Autowired
    private EzK8sV2Client ezK8sV2Client;
    @Autowired
    private EzProjectClient ezProjectClient;

    @Value("${system.cmdb.isEnable}")
    private Boolean isEnable;

    @Override
    public String isPass(RepoVo repo, String clusterKey, String nameSpace, JobRecord jobRecord) {
        if (!SystemConstant.companyIds.contains(repo.getCompanyId()) || !isEnable) {
            return null;
        }
        EzK8sWebClientResponse ezk8sResponse = ezK8sV2Client.brief(clusterKey);
        if (null == ezk8sResponse || ezk8sResponse.getCode() != 0) {
            return String.format("请求k8s服务异常 请求参数：%s 请求结果：%s", clusterKey, ObjectUtils.isEmpty(ezk8sResponse) ? null : ezk8sResponse.getMessage());
        }
        EzProjectPayload ezProjectPayload = EzProjectPayload.builder()
                .clusterKey(ezk8sResponse.getData().getName())
                .companyId(repo.getCompanyId())
                .nameSpace(nameSpace)
                .repoId(repo.getLongRepoKey())
                .repoName(repo.getRepoName())
                .build();
        BaseResponse projectResponse = ezProjectClient.isPassPipeline(ezProjectPayload);
        if (null == projectResponse || projectResponse.getCode() != 0) {
            return String.format("请求project服务异常 请求参数：%s 请求结果：%s", JSONObject.toJSON(ezProjectPayload), ObjectUtils.isEmpty(ezk8sResponse) ? null : ezk8sResponse.getMessage());
        }
        return ObjectUtils.isEmpty(projectResponse.getData()) ? null : projectResponse.getData().toString();
    }
}

