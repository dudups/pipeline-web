package com.ezone.devops.pipeline.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebHookPermission {

    String hookId() default "#hookId";

    String repoName() default "#repoName";

}
