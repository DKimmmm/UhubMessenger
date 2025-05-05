package com.example.uhabmessenger.aop;

import com.example.uhabmessenger.dto.register.LoginDto;
import com.example.uhabmessenger.service.user.authorization.TokenInjectionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthControllerAop {

    private static final Logger log = LoggerFactory.getLogger(AuthControllerAop.class);

    @Pointcut("execution(* com.example.uhabmessenger.controller.auth.AuthorizationController.signUp(..))")
    public void userSignUp(){}

    @Around("userSignUp()")
    public Object aroundUserSignUp(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("<<<<AROUND BEFORE SIGNUP>>>>");
        Object[] args = joinPoint.getArgs();
        log.info("signUp dto is {}", args[0].toString());
        Object result = joinPoint.proceed(args);
        log.info("<<<<AROUND AFTER SIGNUP>>>>>");
        return result;
    }

    @Before("userSignUp()")
    public void beforeUserSignUp() {
        log.info("======= before signup =======");
    }

    @AfterThrowing("userSignUp()")
    public void afterThrowing() {
        log.info("====== was be exception ======");
    }

    @Pointcut("execution(* com.example.uhabmessenger.controller.auth.AuthorizationController.login(..))")
    public void pointcutLogin(){}

    @After("pointcutLogin()")
    public void beforeLogin(JoinPoint joinPoint) {
        log.info("======= after login ======= {}", joinPoint.getArgs()[0].toString());
    }

    @Pointcut("execution(* com.example.uhabmessenger.service.user.authorization.AuthUserServiceImpl.signup(..)) " +
                    "|| execution(* com.example.uhabmessenger.service.user.authorization.AuthUserServiceImpl.login(..))")
    public void createAuthTokenHeader() {

    }

    private final TokenInjectionService tokenInjectionService;

    @Around("createAuthTokenHeader()")
    public Object createTokenIntoResponse(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        Object proceed = joinPoint.proceed(args);
        if (args.length == 1) {
            Object dto = args[0];
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            if (response != null && dto instanceof LoginDto(String username, String password)) {
                tokenInjectionService.jwtInjection(response, username, password);
            } else {
                log.warn("HttpServletResponse is not available in request context");
            }
        } else {
            log.warn("!!!!!!!!!!! where is authorization body !!!!!!!!!");
            log.info("args: {}", Arrays.toString(args));
        }
        return proceed;
    }

}
