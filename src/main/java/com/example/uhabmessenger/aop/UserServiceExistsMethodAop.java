package com.example.uhabmessenger.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Aspect
@Component
public class UserServiceExistsMethodAop {

    private static final Logger log = LoggerFactory.getLogger(UserServiceExistsMethodAop.class);

    private final PlatformTransactionManager transactionManager;

    public UserServiceExistsMethodAop(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Pointcut("execution(boolean com.example.uhabmessenger.repository.*.*(String))")
    public void cutCheckOnExists(){}

    @Before("cutCheckOnExists()")
    public void createTransactionBeforeCheck() {
        log.info("<<<<<<<<<< BEFORE CHECK ON EXISTS >>>>>>>>>>");
    }

    @Around("cutCheckOnExists()")
    public Object transactionCoverMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(" = = = = = = = Cut before transaction start = = = = = = =");
        TransactionStatus trStatus = createTransactionRequestNew();
        Object proceed = joinPoint.proceed();
        commit(trStatus);
        log.info(" = = = = = = = After transaction finish = = = = = = =");
        return proceed;
    }

    private TransactionStatus createTransactionRequestNew() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionDefinition.setName("ExistsTransaction");
        return transactionManager.getTransaction(transactionDefinition);

    }

    private void commit(TransactionStatus transactionStatus) {
        transactionManager.commit(transactionStatus);
    }
}
