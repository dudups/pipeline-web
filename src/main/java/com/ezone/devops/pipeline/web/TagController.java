package com.ezone.devops.pipeline.web;

import com.ezone.devops.ezcode.sdk.service.InternalTagService;
import com.ezone.devops.pipeline.annotations.RepoPermission;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
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

import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Api(tags = "tag搜索api")
@Validated
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private InternalTagService tagService;

    @RepoPermission(requiredPermission = RepoPermissionType.PIPELINE_VIEW)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("搜索代码库下的分支")
    @GetMapping
    public BaseResponse<?> suggest(@RequestParam @NotBlank String repoName,
                                   @RequestParam(required = false) String tagName) {
        RepoVo repo = RepoContext.get();
        List<String> tags = tagService.listTagNames(repo.getCompanyId(), repo.getLongRepoKey(), tagName);
        return new BaseResponse<>(HttpCode.SUCCESS, tags);
    }

}

