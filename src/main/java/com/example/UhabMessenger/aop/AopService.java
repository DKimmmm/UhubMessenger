package com.example.UhabMessenger.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopService {

    private static final Logger log = LoggerFactory.getLogger(AopService.class);

    @Pointcut("execution(* com.example.UhabMessenger.controller.AuthorizationController+)")
    public void userSignUp(){}

    @Around("userSignUp()")
    public Object aroundUserSignUp(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("<<<<<<<<<<<AROUND BEFORE SIGNUP>>>>>>>>>>>>>>");
        Object[] args = joinPoint.getArgs();
        log.info("signUp dto is {}", args[0].toString());
        Object result = joinPoint.proceed(args);
        log.info("<<<<<<<<<<<AROUND AFTER SIGNUP>>>>>>>>>>>>>>");
        return result;
    }

}
