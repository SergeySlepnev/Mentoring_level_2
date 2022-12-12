package com.spdev.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CommonServiceAspect {

    /**
     * Logging findAll() methods in service layer
     */
    @Before(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                    "&& com.spdev.aop.CommonAspect.isFindAllMethodInServiceLayer() " +
                    "&& target(service) " +
                    "&& args(filter, pageable)",
            argNames = "service,filter,pageable")
    public void addLoggingBeforeFindAll(Object service,
                                        Object filter,
                                        Object pageable) {
        log.info("Before - invoked findAll() method in: {}, with params: {}, {}", service, filter, pageable);
    }

    @AfterReturning(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                            "&& com.spdev.aop.CommonAspect.isFindAllMethodInServiceLayer() " +
                            "&& target(service)",
            returning = "resultList",
            argNames = "service,resultList")
    public void addLoggingAfterFindAll(Object service,
                                       Object resultList) {
        log.info("After returning - invoked findAll() method in: {}, result: {}", service, resultList);
    }

    /**
     * Logging findById(id) methods in service layer except for UserService(@PreAuthorize)
     */
    @Before(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                    "&& !com.spdev.aop.CommonAspect.isUserService() " +
                    "&& com.spdev.aop.CommonAspect.isFindByIdMethodInServiceLayer() " +
                    "&& target(service) " +
                    "&& args(id)",
            argNames = "service,id")
    public void addLoggingBeforeFindById(Object service,
                                         Object id) {
        log.info("Before - invoked findById(id) method in: {}, with id: {}", service, id);
    }

    @AfterReturning(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                            "&& !com.spdev.aop.CommonAspect.isUserService() " +
                            "&& com.spdev.aop.CommonAspect.isFindByIdMethodInServiceLayer() " +
                            "&& target(service)",
            returning = "readDto",
            argNames = "service,readDto")
    public void addLoggingAfterFindById(Object service,
                                        Object readDto) {
        log.info("After returning - invoked findById(id) method in: {}, result: {}", service, readDto);
    }

    /**
     * Logging create(*, *) methods in service layer, except for HotelService.create(*,*,*)
     */
    @Before(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                    "&& com.spdev.aop.CommonAspect.isCreateMethodInServiceLayer() " +
                    "&& target(service) " +
                    "&& args(id, createEditDto)",
            argNames = "service,id,createEditDto")
    public void addLoggingBeforeCreate(Object service,
                                       Object id,
                                       Object createEditDto) {
        log.info("Before - invoked create() method in: {}, with params: {}, {}", service, id, createEditDto);
    }

    @AfterReturning(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                            "&& com.spdev.aop.CommonAspect.isCreateMethodInServiceLayer() " +
                            "&& target(service)",
            returning = "readDto",
            argNames = "service,readDto")
    public void addLoggingAfterCreate(Object service,
                                      Object readDto) {
        log.info("After returning - invoked create(id) method in: {}, result: {}", service, readDto);
    }

    /**
     * Logging update(*, *) methods in service layer, except for HotelService.update(*,*,*)
     */
    @Before(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                    "&& com.spdev.aop.CommonAspect.isUpdateMethodInServiceLayer() " +
                    "&& target(service) " +
                    "&& args(id, createEditDto)",
            argNames = "service,id,createEditDto")
    public void addLoggingBeforeUpdate(Object service,
                                       Object id,
                                       Object createEditDto) {
        log.info("Before - invoked update() method in: {}, with params: {}, {}", service, id, createEditDto);
    }

    @AfterReturning(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                            "&& com.spdev.aop.CommonAspect.isUpdateMethodInServiceLayer() " +
                            "&& target(service)",
            returning = "readDto",
            argNames = "service,readDto")
    public void addLoggingAfterUpdate(Object service,
                                      Object readDto) {
        log.info("After returning - invoked update(id) method in: {}, result: {}", service, readDto);
    }

    /**
     * Logging delete(id) methods in service layer
     */
    @Before(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                    "&& com.spdev.aop.CommonAspect.isDeleteMethodInServiceLayer() " +
                    "&& target(service) " +
                    "&& args(id)",
            argNames = "service,id")
    public void addLoggingBeforeDelete(Object service,
                                       Object id) {
        log.info("Before - invoked delete(id) method in: {}, with params: {}", service, id);
    }

    @AfterReturning(value = "com.spdev.aop.CommonAspect.isServiceLayer() " +
                            "&& com.spdev.aop.CommonAspect.isUpdateMethodInServiceLayer() " +
                            "&& target(service)",
            returning = "isDeleted",
            argNames = "service,isDeleted")
    public void addLoggingAfterDelete(Object service,
                                      boolean isDeleted) {
        log.info("After returning - invoked delete(id) method in: {}, result: {}", service, isDeleted);
    }
}