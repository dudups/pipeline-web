package com.ezone.devops.measure.web;

import com.ezone.devops.ezcode.sdk.bean.enums.Dimension;
import com.ezone.devops.measure.beans.Measure;
import com.ezone.devops.measure.service.MeasureService;
import com.ezone.devops.pipeline.annotations.RepoPermission;
import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "代码库构建统计api")
@Validated
@RestController
@RequestMapping("/measures")
public class MeasureController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RepoService repoService;
    @Autowired
    private MeasureService measureService;

    @RepoPermission(requiredPermission = RepoPermissionType.PIPELINE_VIEW)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询代码库下数据统计")
    @GetMapping
    public BaseResponse<?> getMeasures(@RequestParam String repoName,
                                       @RequestParam Integer year,
                                       @RequestParam(defaultValue = "WEEK") Dimension dimension,
                                       @RequestParam Integer value) {
        RepoVo repo = repoService.getRepoByNameIfPresent(authUtil.getCompanyId(), repoName);
        Measure measure = measureService.getAllMeasureByRepo(repo.getRepoKey(), year, dimension, value);
        return new BaseResponse<>(HttpCode.SUCCESS, measure);
    }

}

