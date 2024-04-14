package com.ezone.devops.pipeline.annotations;

import com.ezone.devops.pipeline.enums.PipelinePermissionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobPermission {

    String pipelineId() default "#pipelineId";

    String jobId() default "#jobId";

    PipelinePermissionType requiredPermission();

}
