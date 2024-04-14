package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.annotations.StagePermission;
import com.ezone.devops.pipeline.annotations.el.ExpressionEvaluator;
import com.ezone.devops.pipeline.context.PipelineContext;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.context.StageRecordContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.service.PipelinePermissionService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.service.StageRecordService;
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
public class StagePermissionInterceptor {

    private final ExpressionEvaluator<Long> pipelineIdEvaluator = new ExpressionEvaluator<>();
    private final ExpressionEvaluator<Long> stageIdEvaluator = new ExpressionEvaluator<>();

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private StageRecordService stageRecordService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;
    @Autowired
    private RepoService repoService;

    @Before("@annotation(com.ezone.devops.pipeline.annotations.StagePermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        StagePermission permission = methodSignature.getMethod().getAnnotation(StagePermission.class);
        if (permission == null) {
            log.info("not found permission type");
            throw new NoPermissionOperateException();
        }

        Long pipelineId = getPipelineId(joinPoint, permission);
        if (pipelineId == null) {
            throw new NoPermissionOperateException();
        }

        Long stageId = getStageId(joinPoint, permission);
        if (stageId == null) {
            throw new NoPermissionOperateException();
        }

        StageRecord stageRecord = stageRecordService.getByIdIfPresent(stageId);
        if (!stageRecord.getPipelineId().equals(pipelineId)) {
            throw new NoPermissionOperateException();
        }

        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        RepoVo repo = repoService.getByRepoKey(pipeline.getCompanyId(), pipeline.getRepoKey());

        StageRecordContext.set(stageRecord);
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

    private Long getPipelineId(JoinPoint joinPoint, StagePermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = pipelineIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return pipelineIdEvaluator.condition(permission.pipelineId(), methodKey, evaluationContext, Long.class);
    }

    private Long getStageId(JoinPoint joinPoint, StagePermission permission) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext evaluationContext = stageIdEvaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), method, joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, joinPoint.getTarget().getClass());
        return stageIdEvaluator.condition(permission.stageId(), methodKey, evaluationContext, Long.class);
    }
}
