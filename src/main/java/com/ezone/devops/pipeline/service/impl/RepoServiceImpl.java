package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.ezcode.sdk.bean.model.InternalRepo;
import com.ezone.devops.ezcode.sdk.service.InternalPermissionService;
import com.ezone.devops.ezcode.sdk.service.InternalRepoService;
import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.exception.RepoNotExistException;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class RepoServiceImpl implements RepoService {

    @Autowired
    private InternalRepoService internalRepoService;
    @Autowired
    private InternalPermissionService internalPermissionService;

    @Override
    public RepoVo getByRepoKey(Long companyId, String repoKey) {
        InternalRepo internalRepo = internalRepoService.getRepo(companyId, repoKey);
        if (internalRepo == null) {
            log.error("get repo by key from ezcode error, resp is null, repo key:{}", repoKey);
            return null;
        }
        return new RepoVo(internalRepo);
    }

    @Override
    public RepoVo getByRepoKeyIfPresent(Long companyId, String repoKey) {
        InternalRepo internalRepo = internalRepoService.getRepo(companyId, repoKey);
        if (internalRepo == null) {
            throw new RepoNotExistException();
        }
        return new RepoVo(internalRepo);
    }

    @Override
    public RepoVo getRepoByName(Long companyId, String repoName) {
        InternalRepo internalRepo = getInternalRepoByName(companyId, repoName);
        if (internalRepo == null) {
            log.error("get repo by name from ezcode error, resp is null, company:{} repo name:{}", companyId, repoName);
            return null;
        }
        return new RepoVo(internalRepo);
    }

    private InternalRepo getInternalRepoByName(Long companyId, String repoName) {
        return internalRepoService.getRepoByFullName(companyId, repoName);
    }

    @Override
    public RepoVo getRepoByNameIfPresent(Long companyId, String repoName) {
        return ensureRepoPresent(getRepoByName(companyId, repoName));
    }

    private RepoVo ensureRepoPresent(RepoVo repoVo) {
        if (repoVo != null) {
            return repoVo;
        }
        throw new RepoNotExistException();
    }

    @Override
    public List<RepoVo> getAllRepos() {
        List<InternalRepo> internalRepos = internalRepoService.listAllRepos(true);
        if (CollectionUtils.isEmpty(internalRepos)) {
            return Collections.emptyList();
        }

        return internalRepos.stream().map(RepoVo::new).sorted(Comparator.comparing(RepoVo::getRepoName)).collect(Collectors.toList());
    }

    @Override
    public List<RepoVo> getReposByKeys(Long companyId, Collection<String> repoKeys) {
        if (CollectionUtils.isEmpty(repoKeys)) {
            return Collections.emptyList();
        }

        Set<Long> repoIds = repoKeys.stream().map(Long::parseLong).collect(Collectors.toSet());
        List<InternalRepo> internalRepos = internalRepoService.listReposByIds(companyId, repoIds);
        if (CollectionUtils.isEmpty(internalRepos)) {
            return Collections.emptyList();
        }

        return internalRepos.stream().map(RepoVo::new).collect(Collectors.toList());
    }

    @Override
    public List<RepoVo> suggestRepos(Long companyId, String username, String keyword, int pageNum, int pageSize) {
        List<InternalRepo> internalRepos = internalRepoService.suggestPipelineRepos(companyId, username, keyword, pageNum, pageSize);
        if (CollectionUtils.isEmpty(internalRepos)) {
            return Collections.emptyList();
        }
        Stream<RepoVo> repoVoStream = internalRepos.stream().map(RepoVo::new);
        return repoVoStream.sorted(Comparator.comparing(RepoVo::getRepoName)).collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission(RepoVo repo, String username, RepoPermissionType repoPermissionType) {
        return internalPermissionService.hasAnyPermissions(repo.getCompanyId(), repo.getLongRepoKey(), username, repoPermissionType.name());
    }

    @Override
    public boolean isRepoAdmin(RepoVo repo, String username) {
        return internalPermissionService.isRepoAdmin(repo.getCompanyId(), repo.getLongRepoKey(), username);
    }

    @Override
    public List<String> listUserRepoPermissions(RepoVo repo, String username) {
        if (Objects.isNull(repo) || StringUtils.isBlank(username)) {
            return Collections.emptyList();
        }

        return internalPermissionService.listUserPermissions(repo.getCompanyId(), repo.getLongRepoKey(), username);
    }
}
