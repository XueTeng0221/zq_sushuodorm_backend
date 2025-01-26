package com.ziqiang.sushuodorm.aop;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ziqiang.sushuodorm.annotation.AuthCheck;
import com.ziqiang.sushuodorm.entity.enums.UserRoleEnum;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.services.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String value = authCheck.value();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        UserItem loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(value);
        if (ObjectUtils.isNotNull(mustRoleEnum)) {
            return joinPoint.proceed();
        }
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (ObjectUtils.isNull(userRoleEnum)) {
            throw new RuntimeException();
        }
        if (UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            return joinPoint.proceed();
        }
        return joinPoint.proceed();
    }
}
