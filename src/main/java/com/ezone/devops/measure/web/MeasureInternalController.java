package com.ezone.devops.measure.web;

import com.ezone.devops.ezcode.sdk.bean.enums.Dimension;
import com.ezone.devops.measure.service.MeasureService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@Api(tags = "代码库构建统计api")
@Validated
@RestController
@RequestMapping("/internal/measures")
public class MeasureInternalController {

    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private MeasureService measureService;

    @ApiOperation("查询企业下流水线的数量")
    @GetMapping("/pipelines")
    public BaseResponse<?> countPipeline(@RequestParam Long companyId) {
        int count = pipelineService.countByCompanyId(companyId);
        return new BaseResponse<>(HttpCode.SUCCESS, count);
    }

    @ApiOperation("查询每个代码库下的job的成功和失败数量")
    @GetMapping
    public BaseResponse<?> getMeasures(@RequestParam Long companyId,
                                       @RequestParam Set<String> repoIds,
                                       @RequestParam Integer year,
                                       @RequestParam(defaultValue = "WEEK") Dimension dimension,
                                       @RequestParam Integer value) {
        if (CollectionUtils.isEmpty(repoIds) || Dimension.isInvalidDimension(dimension, year, value)) {
            return new BaseResponse<>(HttpCode.SUCCESS, null);
        }
        List<RepoVo> repos = repoService.getReposByKeys(companyId, repoIds);
        return new BaseResponse<>(HttpCode.SUCCESS, measureService.getByRepos(repos, year, dimension, value));
    }

    @ApiOperation("查询代码库下的按照插件分类的统计")
    @GetMapping("/plugin_types")
    public BaseResponse<?> getMeasuresByPluginType(@RequestParam Long companyId,
                                                   @RequestParam Set<String> repoIds,
                                                   @RequestParam Integer year,
                                                   @RequestParam(defaultValue = "WEEK") Dimension dimension,
                                                   @RequestParam Integer value) {
        if (CollectionUtils.isEmpty(repoIds) || Dimension.isInvalidDimension(dimension, year, value)) {
            return new BaseResponse<>(HttpCode.SUCCESS, null);
        }
        List<RepoVo> repos = repoService.getReposByKeys(companyId, repoIds);
        return new BaseResponse<>(HttpCode.SUCCESS, measureService.getJobCountGroupByPluginTypeQueryRepo(repos, year, dimension, value));
    }

    @ApiOperation("手动触发计算数据统计")
    @GetMapping("/calc")
    public BaseResponse<?> calcMeasures(@RequestParam Long companyId, @RequestParam String repoName) {
        RepoVo repo = repoService.getRepoByNameIfPresent(companyId, repoName);
        measureService.calcCurrentMeasure(repo.getRepoKey());
        return new BaseResponse<>(HttpCode.SUCCESS);
    }

}

