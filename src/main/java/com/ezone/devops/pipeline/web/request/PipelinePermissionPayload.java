package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.vo.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class PipelinePermissionPayload {

    private boolean inheritRepoPermission;
    private boolean allowEdit;

    private Set<UserInfo> admins;
    private Set<UserInfo> maintainers;
    private Set<UserInfo> operators;
    private Set<UserInfo> readers;

    public PipelinePermissionPayload(boolean inheritRepoPermission, boolean allowEdit, Set<UserInfo> admins, Set<UserInfo> maintainers, Set<UserInfo> operators, Set<UserInfo> readers) {
        this.inheritRepoPermission = inheritRepoPermission;
        this.allowEdit = allowEdit;
        this.admins = admins;
        this.maintainers = maintainers;
        this.operators = operators;
        this.readers = readers;
    }
}
