package com.ezone.devops.pipeline.annotations.aop;

import com.ezone.devops.pipeline.annotations.CompanyAdminPermission;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
import com.ezone.ezbase.iam.bean.enums.UserIdentityType;
import com.ezone.ezbase.iam.service.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CompanyAdminPermissionInterceptor {

    @Autowired
    private AuthUtil authUtil;

    @Before("@annotation(com.ezone.devops.pipeline.annotations.CompanyAdminPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        CompanyAdminPermission permission = methodSignature.getMethod().getAnnotation(CompanyAdminPermission.class);
        if (permission == null) {
            log.info("not found permission type");
            throw new NoPermissionOperateException();
        }

        // 企业管理员
        UserIdentityType userIdentityType = authUtil.getUserIdentityType();
        if (userIdentityType == UserIdentityType.ADMIN) {
            return;
        }

        throw new NoPermissionOperateException();
    }
}
