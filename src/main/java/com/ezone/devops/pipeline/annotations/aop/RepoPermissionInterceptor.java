package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.annotations.RepoPermission;
import com.ezone.devops.pipeline.annotations.el.ExpressionEvaluator;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
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
public class RepoPermissionInterceptor {

    private final ExpressionEvaluator<String> repoEvaluator = new ExpressionEvaluator<>();

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RepoService repoService;

    @Before("@annotation(com.ezone.devops.pipeline.annotations.RepoPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RepoPermission repoPermission = methodSignature.getMethod().getAnnotation(RepoPermission.class);
        if (repoPermission == null) {
            log.info("not found repoPermission type");
            throw new NoPermissionOperateException();
        }

        Long companyId = authUtil.getCompanyId();
        String repoName = getRepoName(joinPoint, repoPermission);

        RepoVo repo = repoService.getRepoByName(companyId, repoName);
        if (repo == null) {
            log.info("repo not exist {}", repoName);
            throw new NoPermissionOperateException();
        }

        RepoContext.set(repo);

        // 企业管理员
        UserIdentityType userIdentityType = authUtil.getUserIdentityType();
        if (userIdentityType == UserIdentityType.ADMIN) {
            return;
        }

        RepoPermissionType requiredPermission = repoPermission.requiredPermission();
        boolean hasPermission = repoService.hasPermission(repo, authUtil.getUsername(), requiredPermission);
        if (hasPermission) {
            return;
        }

        throw new NoPermissionOperateException();
    }

    private String getRepoName(JoinPoint joinPoint, RepoPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = repoEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return repoEvaluator.condition(permission.repoName(), methodKey, evaluationContext, String.class);
    }
}
