package com.spdev.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CommonAspect {

    @Pointcut("within(com.spdev.service.*Service)")
    public void isServiceLayer() {
    }

    @Pointcut("within(com.spdev.service.UserService)")
    public void isUserService() {
    }

    @Pointcut("execution(public * com.spdev.service.*Service.findAll(*, *))")
    public void isFindAllMethodInServiceLayer() {
    }

    @Pointcut("execution(public * com.spdev.service.*Service.findById(*))")
    public void isFindByIdMethodInServiceLayer() {
    }

    @Pointcut("execution(public * com.spdev.service.*Service.create(*))")
    public void isCreateMethodInServiceLayer() {
    }

    @Pointcut("execution(public * com.spdev.service.*Service.udpdate(*, *))")
    public void isUpdateMethodInServiceLayer() {
    }

    @Pointcut("execution(public * com.spdev.service.*Service.delete(*))")
    public void isDeleteMethodInServiceLayer() {
    }
}