package com.ezone.devops.plugins.web;

import com.ezone.devops.plugins.service.BuildImageService;
import com.ezone.devops.plugins.service.PluginService;
import com.ezone.devops.plugins.web.request.BuildImagePayload;
import com.ezone.devops.plugins.web.request.PluginCallbackPayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "内部使用接口")
@RestController
@RequestMapping("/internal")
public class PluginInternalController {

    @Autowired
    private PluginService pluginService;
    @Autowired
    private BuildImageService buildImageService;

    @ApiOperation("镜像列表")
    @GetMapping("/jobs/{job}/images")
    public BaseResponse<?> listImage(@PathVariable String job) {
        return new BaseResponse<>(HttpCode.SUCCESS, buildImageService.listBuildImage(job));
    }

    @ApiOperation("添加镜像")
    @PostMapping("/jobs/{job}/images")
    public BaseResponse<?> createImage(@PathVariable String job,
                                       @RequestBody BuildImagePayload payload) {
        return new BaseResponse<>(HttpCode.SUCCESS, buildImageService.createBuildImage(job, payload));
    }

    @ApiOperation("更新镜像")
    @PutMapping("/jobs/{job}/images/{id}")
    public BaseResponse<?> updateImage(@PathVariable String job,
                                       @PathVariable Long id,
                                       @RequestBody BuildImagePayload payload) {
        return new BaseResponse<>(HttpCode.SUCCESS, buildImageService.updateBuildImage(job, id, payload));
    }

    @ApiOperation("删除镜像")
    @DeleteMapping("/jobs/{job}/images/{id}")
    public BaseResponse<?> deleteImage(@PathVariable String job,
                                       @PathVariable Long id) {
        return new BaseResponse<>(HttpCode.SUCCESS, buildImageService.deleteBuildImage(job, id));
    }

    @ApiOperation(value = "查询所有插件")
    @GetMapping("/plugins")
    public BaseResponse<?> listPlugins() {
        return new BaseResponse<>(HttpCode.SUCCESS, pluginService.listPlugins());
    }

    @ApiOperation(value = "插件执行状态统一回调接口")
    @PostMapping("/pipelines/{pipelineId}/jobs/{jobId}")
    public BaseResponse<Boolean> executeCallback(@PathVariable Long pipelineId,
                                                 @PathVariable Long jobId,
                                                 @RequestBody @Validated PluginCallbackPayload payload) {
        boolean result = pluginService.handlePluginBuildCallback(pipelineId, jobId, payload);
        return new BaseResponse<>(HttpCode.SUCCESS, result);
    }
}

