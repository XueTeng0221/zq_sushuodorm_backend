package com.ziqiang.sushuodorm.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthInterceptor {
//    @Resource
//    private UserService userService;
//
//    @Around("@annotation(authCheck)")
//    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
//        String value = authCheck.value();
//        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        UserVo loginUser = userService.getLoginUser(value, value, request);
//        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(value);
//        if (ObjectUtils.isNotNull(mustRoleEnum)) {
//            return joinPoint.proceed();
//        }
//        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole().getValue());
//        if (ObjectUtils.isNull(userRoleEnum)) {
//            throw new RuntimeException();
//        }
//        return joinPoint.proceed();
//    }
}
