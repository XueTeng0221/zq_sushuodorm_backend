package com.ziqiang.sushuodorm.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LogInterceptor {
    @Around("execution(*com.ziqiang.sushuodorm.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String requestId = UUID.randomUUID().toString();
        String url = request.getRequestURI();
        Object[] args = point.getArgs();
        String requestParam = "[" + StringUtils.join(args, ", ") + "]";
        log.info("start request. requestId: {}, url: {}, ip: {}, param: {}", requestId, url, request.getRemoteHost(), requestParam);
        Object result = point.proceed();
        stopWatch.stop();
        log.info("end request. requestId: {}, cost: {}ms", requestId, stopWatch.getTotalTimeMillis());
        return result;
    }
}
