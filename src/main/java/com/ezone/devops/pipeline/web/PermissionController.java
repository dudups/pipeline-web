package com.ezone.devops.pipeline.web;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "流水线权限")
@Slf4j
@Valid
@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RepoService repoService;

    @ApiOperation("查询一个人在代码库中的权限")
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping
    public BaseResponse<?> getPermission(@RequestParam @NotNull String repoName) {
        Long companyId = authUtil.getCompanyId();
        RepoVo repo = repoService.getRepoByNameIfPresent(companyId, repoName);

        List<String> permissions = repoService.listUserRepoPermissions(repo, authUtil.getUsername());
        return new BaseResponse<>(HttpCode.SUCCESS, permissions);
    }
}

