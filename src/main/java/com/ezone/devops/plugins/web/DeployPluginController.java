package com.ezone.devops.plugins.web;

import com.ezone.devops.pipeline.clients.EzK8sClient;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "部署插件api")
@RestController
@Validated
@RequestMapping("/plugins/deploy")
public class DeployPluginController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private EzK8sClient ezK8sClient;

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "搜索企业下的所有集群")
    @GetMapping("/{jobType}/clusters")
    public BaseResponse<?> suggestClusters(@PathVariable String jobType,
                                           @RequestParam(required = false) String name) {
        return ezK8sClient.suggestClusterByName(authUtil.getCompanyId(), name);
    }

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "获取集群下的所有环境")
    @GetMapping("/{jobType}/clusters/{clusterName}")
    public BaseResponse<?> listEnvs(@PathVariable String jobType,
                                    @PathVariable String clusterName) {
        return ezK8sClient.listEnvs(authUtil.getCompanyId(), clusterName);
    }

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "获取环境下的所有部署模板")
    @GetMapping("/{jobType}/clusters/{clusterName}/envs/{envName}")
    public BaseResponse<?> listEnvs(@PathVariable String jobType,
                                    @PathVariable String clusterName,
                                    @PathVariable String envName,
                                    @RequestParam(required = false) String name) {
        return ezK8sClient.suggestDeployConfigs(authUtil.getCompanyId(), clusterName, envName, name);
    }

}

