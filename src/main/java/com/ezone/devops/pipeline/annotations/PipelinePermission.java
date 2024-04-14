package com.ezone.devops.pipeline.annotations;

import com.ezone.devops.pipeline.enums.PipelinePermissionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelinePermission {

    String pipelineId() default "#pipelineId";

    PipelinePermissionType requiredPermission();

}
