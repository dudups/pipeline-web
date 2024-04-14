package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.annotations.PipelineRecordPermission;
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
public class PipelineRecordPermissionInterceptor {

    private final ExpressionEvaluator<Long> pipelineIdEvaluator = new ExpressionEvaluator<>();
    private final ExpressionEvaluator<Long> recordIdEvaluator = new ExpressionEvaluator<>();

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;
    @Autowired
    private PipelineRecordService pipelineRecordService;

    @Before("@annotation(com.ezone.devops.pipeline.annotations.PipelineRecordPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        PipelineRecordPermission permission = methodSignature.getMethod().getAnnotation(PipelineRecordPermission.class);
        if (permission == null) {
            log.info("not found permission type");
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

        if (!pipelineRecord.getPipelineId().equals(pipelineId)) {
            throw new NoPermissionOperateException();
        }

        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        Long companyId = pipeline.getCompanyId();
        String username = authUtil.getUsername();
        RepoVo repo = repoService.getByRepoKeyIfPresent(companyId, pipeline.getRepoKey());

        // 企业管理员
        UserIdentityType userIdentityType = authUtil.getUserIdentityType();
        if (userIdentityType == UserIdentityType.ADMIN || pipelinePermissionService.hasPermission(repo, pipeline, permission.requiredPermission(), username)) {
            PipelineRecordContext.set(pipelineRecord);
            PipelineContext.set(pipeline);
            RepoContext.set(repo);
            return;
        }

        throw new NoPermissionOperateException();
    }

    private Long getPipelineId(JoinPoint joinPoint, PipelineRecordPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = pipelineIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return pipelineIdEvaluator.condition(permission.pipelineId(), methodKey, evaluationContext, Long.class);
    }

    private Long getRecordId(JoinPoint joinPoint, PipelineRecordPermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = recordIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return recordIdEvaluator.condition(permission.recordId(), methodKey, evaluationContext, Long.class);
    }
}
