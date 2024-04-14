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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Api(tags = "代码库搜索api")
@Validated
@RestController
@RequestMapping("/repos")
public class RepoController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RepoService repoService;

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询代码库列表")
    @GetMapping("/suggest")
    public BaseResponse<?> suggest(@RequestParam(required = false) String keyword,
                                   @RequestParam(defaultValue = "1") @Min(1) int pageNumber,
                                   @RequestParam(defaultValue = "10") @Min(1) @Max(30) int pageSize) {
        Long companyId = authUtil.getCompanyId();
        String username = authUtil.getUsername();
        List<RepoVo> repos = repoService.suggestRepos(companyId, username, keyword, pageNumber, pageSize);
        return new BaseResponse<>(HttpCode.SUCCESS, repos);
    }
}

