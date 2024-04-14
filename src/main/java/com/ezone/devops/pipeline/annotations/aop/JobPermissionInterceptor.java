package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.annotations.JobPermission;
import com.ezone.devops.pipeline.annotations.el.ExpressionEvaluator;
import com.ezone.devops.pipeline.context.JobRecordContext;
import com.ezone.devops.pipeline.context.PipelineContext;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.service.JobRecordService;
import com.ezone.devops.pipeline.service.PipelinePermissionService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.enums.UserIdentityType;
import com.ezone.ezbase.iam.service.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class JobPermissionInterceptor {

    private final ExpressionEvaluator<Long> pipelineIdEvaluator = new ExpressionEvaluator<>();
    private final ExpressionEvaluator<Long> jobIdEvaluator = new ExpressionEvaluator<>();

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private JobRecordService jobRecordService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;
    @Autowired
    private RepoService repoService;

    @Before("@annotation(com.ezone.devops.pipeline.annotations.JobPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        JobPermission permission = methodSignature.getMethod().getAnnotation(JobPermission.class);
        if (permission == null) {
            log.info("not found permission type");
            throw new NoPermissionOperateException();
        }

        Long pipelineId = getPipelineId(joinPoint, permission);
        if (pipelineId == null) {
            throw new NoPermissionOperateException();
        }

        Long jobId = getJobId(joinPoint, permission);
        if (jobId == null) {
            throw new NoPermissionOperateException();
        }

        JobRecord jobRecord = jobRecordService.getByIdIfPresent(jobId);
        if (!jobRecord.getPipelineId().equals(pipelineId)) {
            throw new NoPermissionOperateException();
        }

        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        RepoVo repo = repoService.getByRepoKey(pipeline.getCompanyId(), pipeline.getRepoKey());

        JobRecordContext.set(jobRecord);
        PipelineContext.set(pipeline);
        RepoContext.set(repo);

        // 企业管理员
        UserIdentityType userIdentityType = authUtil.getUserIdentityType();
        if (userIdentityType == UserIdentityType.ADMIN) {
            return;
        }

        PipelinePermissionType pipelinePermissionType = permission.requiredPermission();
        boolean hasPermission = pipelinePermissionService.hasPermission(repo, pipeline, pipelinePermissionType, authUtil.getUsername());
        if (hasPermission) {
            return;
        }

        throw new NoPermissionOperateException();
    }

    private Long getPipelineId(JoinPoint joinPoint, JobPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = pipelineIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return pipelineIdEvaluator.condition(permission.pipelineId(), methodKey, evaluationContext, Long.class);
    }

    private Long getJobId(JoinPoint joinPoint, JobPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = jobIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return jobIdEvaluator.condition(permission.jobId(), methodKey, evaluationContext, Long.class);
    }
}
