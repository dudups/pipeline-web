package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.context.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CleanUpInterceptor {

    @AfterReturning("@annotation(com.ezone.devops.pipeline.annotations.PipelinePermission)")
    public void cleanPipeline(JoinPoint joinPoint) {
        cleaUp();
    }

    @AfterReturning("@annotation(com.ezone.devops.pipeline.annotations.PipelineRecordPermission)")
    public void cleanPipelineRecord(JoinPoint joinPoint) {
        cleaUp();
    }

    @AfterReturning("@annotation(com.ezone.devops.pipeline.annotations.StagePermission)")
    public void cleanStage(JoinPoint joinPoint) {
        cleaUp();
    }

    @AfterReturning("@annotation(com.ezone.devops.pipeline.annotations.JobPermission)")
    public void cleanJob(JoinPoint joinPoint) {
        cleaUp();
    }

    @AfterReturning("@annotation(com.ezone.devops.pipeline.annotations.RepoPermission)")
    public void cleanRepo(JoinPoint joinPoint) {
        cleaUp();
    }

    @AfterReturning("@annotation(com.ezone.devops.pipeline.annotations.WebHookPermission)")
    public void cleanWebHook(JoinPoint joinPoint) {
        cleaUp();
    }

    private void cleaUp() {
        PipelineContext.remove();
        PipelineRecordContext.remove();
        StageRecordContext.remove();
        JobRecordContext.remove();
        RepoContext.remove();
    }
}
