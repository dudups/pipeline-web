package com.ezone.devops.pipeline.annotations;

import com.ezone.devops.pipeline.enums.PipelinePermissionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelineRecordPermission {

    String pipelineId() default "#pipelineId";

    String recordId() default "#recordId";

    PipelinePermissionType requiredPermission();

}
