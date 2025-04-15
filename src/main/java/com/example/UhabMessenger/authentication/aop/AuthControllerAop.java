package com.example.UhabMessenger.authentication.aop;

import com.example.UhabMessenger.userdata.dto.register.LoginDto;
import com.example.UhabMessenger.userdata.service.user.authorization.TokenInjectionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthControllerAop {

    private static final Logger log = LoggerFactory.getLogger(AuthControllerAop.class);

    @Pointcut("execution(* com.example.UhabMessenger.authentication.controller.AuthorizationController.signUp(..))")
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
        log.info("============== before signup ==============");
    }

    @AfterThrowing("userSignUp()")
    public void afterThrowing() {
        log.info("======== was be exception ========");
    }

    @Pointcut("execution(* com.example.UhabMessenger..AuthorizationController.login(..))")
    public void pointcutLogin(){}

    @After("pointcutLogin()")
    public void beforeLogin(JoinPoint joinPoint) {
        log.info("============== after login ============== {}", joinPoint.getArgs()[0].toString());
    }

    @Pointcut("execution(* com.example.UhabMessenger.authentication.service.authorization.AuthUserService.signup(..)) " +
                    "|| execution(* com.example.UhabMessenger.authentication.service.authorization.AuthUserService.login(..))")
    public void createAuthTokenHeader() {

    }

    private final TokenInjectionService tokenInjectionService;

    @Around("createAuthTokenHeader()")
    public Object createTokenIntoResponse(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        Object proceed = joinPoint.proceed(args);
        if (args.length >= 2) {
            Object arg1 = args[0];
            Object arg2 = args[1];
            if (arg2 instanceof HttpServletResponse response && arg1 instanceof LoginDto dto) {
                tokenInjectionService.jwtInjection(response, dto.username(), dto.password());
            }
        } else {
            log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!! where is authorization body !!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.info("args: {}", Arrays.toString(args));
        }
        return proceed;
    }

}
