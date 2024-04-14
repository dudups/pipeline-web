package com.ezone.devops.plugins.web;

import com.ezone.devops.plugins.service.BuildImageService;
import com.ezone.devops.plugins.service.PluginService;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "插件通用api")
@RestController
@RequestMapping("/plugins")
public class PluginController {

    @Autowired
    private PluginService pluginService;
    @Autowired
    private BuildImageService buildImageService;

    @ApiOperation(value = "查询所有插件")
    @GetMapping
    public BaseResponse<?> listPlugins() {
        return new BaseResponse<>(HttpCode.SUCCESS, pluginService.listPlugins());
    }

    @ApiOperation(value = "按job类型查询镜像")
    @GetMapping("/{jobType}/images")
    public BaseResponse<?> listBuildImage(@PathVariable String jobType) {
        return new BaseResponse<>(HttpCode.SUCCESS, buildImageService.listBuildImage(jobType));
    }
}

