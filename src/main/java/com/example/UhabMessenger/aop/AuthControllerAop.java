package com.example.UhabMessenger.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthControllerAop {

    private static final Logger log = LoggerFactory.getLogger(AuthControllerAop.class);

    @Pointcut("execution(* com.example.UhabMessenger.controller.AuthorizationController.signUp(..))")
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

    @Before("userSignUp()")
    public void beforeUserSignUp() {
        log.info("======before======");
    }

    @AfterThrowing("userSignUp()")
    public void afterThrowing() {
        log.info("======== was be exception ========");
    }

    @Pointcut("execution(* com.example.UhabMessenger.controller.AuthorizationController.login(..))")
    public void pointcutLogin(){}

    @After("pointcutLogin()")
    public void beforeLogin(JoinPoint joinPoint) {
        log.info("<<<<<<<<<<< AFTER USER LOGIN >>>>>>>>>>>> {}", joinPoint.getArgs()[0].toString());
    }

}
