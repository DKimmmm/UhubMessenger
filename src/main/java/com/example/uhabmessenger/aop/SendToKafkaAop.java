package com.example.uhabmessenger.aop;

import com.example.uhabmessenger.annotation.KafkaAsyncLogging;
import com.example.uhabmessenger.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SendToKafkaAop {

    private final KafkaProducerService kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;

    @Pointcut("@annotation(com.example.KafkaAsyncLogging)")
    public void loggingForKafka(){}

    @SneakyThrows
    @Around("@annotation(kafkaAnnotation)")
    public Object afterLoggingForKafka(ProceedingJoinPoint joinPoint, KafkaAsyncLogging kafkaAnnotation) {

        Object[] args = joinPoint.getArgs();

        int argNumber = kafkaAnnotation.sendArgNumber();

        if (args.length < argNumber + 1 || argNumber < 0) {
            throw new IllegalArgumentException("не правильная передача аргумента в аннотацию для отправки в kafka");
        }

        Object payload = args[argNumber];

        String sendMessage = kafkaAnnotation.sendMessage() + payload.toString();

        kafkaTemplate.sendMessage(kafkaTopic, sendMessage);

        // Выполнение исходного метода
        return joinPoint.proceed();

    }
//    @SneakyThrows
//    @Around("loggingForKafka()")
//    public Object afterLoggingForKafka(ProceedingJoinPoint joinPoint, KafkaAsyncLogging kafkaAnnotation) {
//
//        Object[] args = joinPoint.getArgs();
//
//        int argNumber = kafkaAnnotation.sendArgNumber();
//
//        if (args.length < argNumber + 1 || argNumber < 0) {
//            throw new IllegalArgumentException("не правильная передача аргумента в аннотацию для отправки в kafka");
//        }
//
//        Object payload = args[argNumber];
//
//        String sendMessage = kafkaAnnotation.sendMessage() + payload.toString();
//
//        kafkaTemplate.sendMessage(kafkaTopic, sendMessage);
//
//        // Выполнение исходного метода
//        return joinPoint.proceed();
//
//    }

}
