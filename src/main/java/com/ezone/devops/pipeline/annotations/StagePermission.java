package com.ezone.devops.pipeline.annotations;

import com.ezone.devops.pipeline.enums.PipelinePermissionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StagePermission {

    String pipelineId() default "#pipelineId";

    String stageId() default "#stageId";

    PipelinePermissionType requiredPermission();

}
