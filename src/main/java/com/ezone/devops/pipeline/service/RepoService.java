package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.vo.RepoVo;

import java.util.Collection;
import java.util.List;

public interface RepoService {

    RepoVo getByRepoKey(Long companyId, String repoKey);

    RepoVo getByRepoKeyIfPresent(Long companyId, String repoKey);

    RepoVo getRepoByName(Long companyId, String repoName);

    RepoVo getRepoByNameIfPresent(Long companyId, String repoName);

    List<RepoVo> getAllRepos();

    List<RepoVo> getReposByKeys(Long companyId, Collection<String> repoKeys);

    List<RepoVo> suggestRepos(Long companyId, String username, String keyword, int pageNum, int pageSize);

    boolean hasPermission(RepoVo repo, String username, RepoPermissionType repoPermissionType);

    boolean isRepoAdmin(RepoVo repo, String username);

    List<String> listUserRepoPermissions(RepoVo repo, String username);
}
