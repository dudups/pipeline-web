package com.ezone.devops.pipeline.vo;

import com.ezone.devops.ezcode.sdk.bean.model.InternalRepo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RepoVo {

    private Long companyId;
    private long dirId;
    private long longRepoKey;
    private String repoKey;
    private String repoName;
    private String repoStatus;

    public RepoVo(InternalRepo internalRepo) {
        setCompanyId(internalRepo.getCompanyId());
        setDirId(internalRepo.getDirId());
        setLongRepoKey(internalRepo.getId());
        setRepoKey(internalRepo.getStringId());
        setRepoName(internalRepo.getRepoName());
        setRepoStatus(internalRepo.getStatus());
    }
}
