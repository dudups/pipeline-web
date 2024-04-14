package com.ezone.devops.report.web;

import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.report.config.ReportSystemConfig;
import com.ezone.devops.report.model.ReportInfo;
import com.ezone.devops.report.service.ReportInfoService;
import com.ezone.ezbase.iam.bean.enums.UserIdentityType;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.exception.PermissionDenyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Paths;

@Controller
@RequestMapping("/result")
public class ReportController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private ReportSystemConfig reportSystemConfig;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private ReportInfoService reportInfoService;

    @RequestMapping("/{reportId}/pages/{**path}")
    public String read(@PathVariable String reportId, @PathVariable String path) {
        ReportInfo reportInfo = reportInfoService.getReportInfoIfPresent(reportId);
        String filePath = reportInfo.getStoragePath() + "/" + path;
        String suffix = getSuffix(path);
        if (reportSystemConfig.getStaticSuffix().contains(suffix)) {
            return "forward:/result/static?filePath=" + filePath;
        }

        checkPermission(reportInfo.getPipelineId());
        return filePath;
    }

    @GetMapping(value = "/static", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<?> getStatic(String filePath) {
        return ResponseEntity.ok(getResource(filePath));
    }

    private Resource getResource(String filePath) {
        String fileFullPath = reportSystemConfig.getBasePath() + "/" + filePath;
        String path = Paths.get(fileFullPath).toString();
        return resourceLoader.getResource("file:" + path);
    }

    private String getSuffix(String path) {
        return StringUtils.substring(path, StringUtils.lastIndexOf(path, ".") + 1, path.length());
    }

    private void checkPermission(Long pipelineId) {
        if (authUtil.getUserIdentityType() == UserIdentityType.ADMIN) {
            return;
        }

        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        RepoVo repo = repoService.getByRepoKey(pipeline.getCompanyId(), pipeline.getRepoKey());
        boolean hasPermission = repoService.hasPermission(repo, authUtil.getUsername(), RepoPermissionType.PIPELINE_VIEW);
        if (!hasPermission) {
            throw new PermissionDenyException();
        }
    }
}
