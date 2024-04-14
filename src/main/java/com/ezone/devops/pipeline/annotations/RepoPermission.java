package com.ezone.devops.pipeline.annotations;

import com.ezone.devops.pipeline.enums.RepoPermissionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepoPermission {

    String repoName() default "#repoName";

    RepoPermissionType requiredPermission();

}
