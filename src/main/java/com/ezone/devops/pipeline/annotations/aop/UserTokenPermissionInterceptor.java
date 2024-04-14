package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.annotations.UserTokenPermission;
import com.ezone.devops.pipeline.annotations.el.ExpressionEvaluator;
import com.ezone.devops.pipeline.context.PipelineContext;
import com.ezone.devops.pipeline.context.PipelineRecordContext;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.service.PipelinePermissionService;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.BaseUser;
import com.ezone.ezbase.iam.service.IAMCenterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class UserTokenPermissionInterceptor {

    private final ExpressionEvaluator<Long> pipelineIdEvaluator = new ExpressionEvaluator<>();
    private final ExpressionEvaluator<Long> recordIdEvaluator = new ExpressionEvaluator<>();

    private static final String USER_TOKEN_HEADER = "access_token";

    @Autowired
    private IAMCenterService iamCenterService;
    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;

    @Before("@annotation(com.ezone.devops.pipeline.annotations.UserTokenPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(USER_TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            throw new NoPermissionOperateException();
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        UserTokenPermission permission = methodSignature.getMethod().getAnnotation(UserTokenPermission.class);
        if (permission == null) {
            throw new NoPermissionOperateException();
        }

        Long pipelineRecordId = getRecordId(joinPoint, permission);
        if (pipelineRecordId == null) {
            throw new NoPermissionOperateException();
        }

        PipelineRecord pipelineRecord = pipelineRecordService.getByIdIfPresent(pipelineRecordId);
        Long pipelineId = getPipelineId(joinPoint, permission);
        if (pipelineId == null) {
            throw new NoPermissionOperateException();
        }

        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        Long companyId = pipeline.getCompanyId();
        RepoVo repo = repoService.getByRepoKeyIfPresent(companyId, pipeline.getRepoKey());

        BaseUser baseUser = iamCenterService.queryUserByToken(token);
        if (baseUser == null) {
            throw new NoPermissionOperateException();
        }

        boolean hasPermission = pipelinePermissionService.hasPermission(repo, pipeline, permission.requiredPermission(), baseUser.getUsername());
        if (hasPermission) {
            PipelineRecordContext.set(pipelineRecord);
            PipelineContext.set(pipeline);
            RepoContext.set(repo);
            return;
        }

        throw new NoPermissionOperateException();
    }

    private Long getPipelineId(JoinPoint joinPoint, UserTokenPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = pipelineIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return pipelineIdEvaluator.condition(permission.pipelineId(), methodKey, evaluationContext, Long.class);
    }

    private Long getRecordId(JoinPoint joinPoint, UserTokenPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = recordIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return recordIdEvaluator.condition(permission.recordId(), methodKey, evaluationContext, Long.class);
    }
}
