package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.annotations.PipelinePermission;
import com.ezone.devops.pipeline.annotations.el.ExpressionEvaluator;
import com.ezone.devops.pipeline.context.PipelineContext;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
import com.ezone.devops.pipeline.model.Pipeline;
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
public class PipelinePermissionInterceptor {

    private final ExpressionEvaluator<Long> pipelineIdEvaluator = new ExpressionEvaluator<>();

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;
    @Autowired
    private PipelineService pipelineService;

    @Before("@annotation(com.ezone.devops.pipeline.annotations.PipelinePermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        PipelinePermission permission = methodSignature.getMethod().getAnnotation(PipelinePermission.class);
        if (permission == null) {
            log.info("not found permission type");
            throw new NoPermissionOperateException();
        }

        Long pipelineId = getPipelineId(joinPoint, permission);
        if (pipelineId == null) {
            throw new NoPermissionOperateException();
        }

        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        PipelineContext.set(pipeline);

        Long companyId = pipeline.getCompanyId();
        String username = authUtil.getUsername();

        RepoVo repo = repoService.getByRepoKeyIfPresent(companyId, pipeline.getRepoKey());
        RepoContext.set(repo);

        // 企业管理员
        UserIdentityType userIdentityType = authUtil.getUserIdentityType();
        if (userIdentityType == UserIdentityType.ADMIN) {
            return;
        }

        PipelinePermissionType pipelinePermissionType = permission.requiredPermission();
        boolean hasPermission = pipelinePermissionService.hasPermission(repo, pipeline, pipelinePermissionType, username);
        if (hasPermission) {
            return;
        }

        throw new NoPermissionOperateException();
    }

    private Long getPipelineId(JoinPoint joinPoint, PipelinePermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = pipelineIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return pipelineIdEvaluator.condition(permission.pipelineId(), methodKey, evaluationContext, Long.class);
    }
}
