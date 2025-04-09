package com.example.UhabMessenger.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserServiceExistsMethodAop {

    private static final Logger log = LoggerFactory.getLogger(UserServiceExistsMethodAop.class);

    @Pointcut("execution(boolean com.example.UhabMessenger.repository.*.*(String))")
    public void cutCheckOnExists(){}

    @Before("cutCheckOnExists()")
    public void createTransactionBeforeCheck() {
        log.info("<<<<<<<<<< BEFORE CHECK ON EXISTS >>>>>>>>>>");
    }

}
