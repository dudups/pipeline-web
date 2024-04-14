package com.ezone.devops.report.acl;

import com.ezone.ezbase.iam.bean.enums.SystemType;
import com.ezone.ezbase.iam.bean.enums.TokenAuthType;
import com.ezone.ezbase.iam.service.IAMCenterService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class OpenApiInterceptor {

    private static final String COMPANY_NAME_HEADER = "sys_company_name";
    private static final String USER_TOKEN_HEADER = "access_token";

    @Autowired
    private IAMCenterService iamCenterService;

    @Before("@annotation(com.ezone.devops.report.acl.OpenApi)")
    public void runnerAcl(JoinPoint jp) throws Exception {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String companyName = request.getHeader(COMPANY_NAME_HEADER);
        String token = request.getHeader(USER_TOKEN_HEADER);
        if (StringUtils.isEmpty(companyName) || StringUtils.isEmpty(token)) {
            throw new NoPermissionException();
        }

        boolean hasPermission = iamCenterService.hasAuthTypePermissionByAccessToken(companyName,
                SystemType.EZPIPELINE, token, TokenAuthType.WRITE);

        if (!hasPermission) {
            throw new NoPermissionException();
        }
    }
}
