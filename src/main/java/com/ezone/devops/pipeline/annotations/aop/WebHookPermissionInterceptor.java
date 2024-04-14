package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.annotations.WebHookPermission;
import com.ezone.devops.pipeline.annotations.el.ExpressionEvaluator;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.enums.UserIdentityType;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.ezbase.iam.service.IAMCenterService;
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
public class WebHookPermissionInterceptor {

    private final ExpressionEvaluator<String> repoEvaluator = new ExpressionEvaluator<>();
    private final ExpressionEvaluator<Long> hookIdEvaluator = new ExpressionEvaluator<>();

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RepoService repoService;
    @Autowired
    private IAMCenterService iamCenterService;

    @Before("@annotation(com.ezone.devops.pipeline.annotations.WebHookPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        WebHookPermission webHookPermission = methodSignature.getMethod().getAnnotation(WebHookPermission.class);
        if (webHookPermission == null) {
            log.info("not found webHookPermission type");
            throw new NoPermissionOperateException();
        }

        Long companyId = authUtil.getCompanyId();
        String repoName = getRepoName(joinPoint, webHookPermission);

        Long hookId = getHookId(joinPoint, webHookPermission);

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

        if (!iamCenterService.isHookAdmin(repo.getCompanyId(), hookId, authUtil.getUserId())) {
            throw new NoPermissionOperateException();
        }

        boolean repoAdmin = repoService.isRepoAdmin(repo, authUtil.getUsername());
        // 是否是代码库管理员
        if (repoAdmin) {
            return;
        }

        throw new NoPermissionOperateException();
    }

    private Long getHookId(JoinPoint joinPoint, WebHookPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = hookIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return hookIdEvaluator.condition(permission.hookId(), methodKey, evaluationContext, Long.class);
    }

    private String getRepoName(JoinPoint joinPoint, WebHookPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = repoEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return repoEvaluator.condition(permission.repoName(), methodKey, evaluationContext, String.class);
    }
}
