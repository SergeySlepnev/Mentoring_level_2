package com.spdev.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class HotelServiceAspect {

    @Pointcut("within(com.spdev.service.HotelService)")
    public void isHotelService() {
    }

    @Pointcut("execution(public * com.spdev.service.HotelService.create(*, *, *))")
    public void isCreateMethodInHotelService() {
    }

    @Pointcut("execution(public * com.spdev.service.HotelService.create(*, *, *))")
    public void isUpdateMethodInHotelService() {
    }

    /**
     * Logging create() method in HotelService
     */
    @Around(value = "isHotelService() " +
                    "&& isCreateMethodInHotelService() " +
                    "&& target(service) " +
                    "&& args(hotelDto, hotelDetailsDto, hotelContentDto)",
            argNames = "joinPoint,service,hotelDto,hotelDetailsDto,hotelContentDto")
    public Object addLoggingAroundCreateHotel(ProceedingJoinPoint joinPoint,
                                              Object service,
                                              Object hotelDto,
                                              Object hotelDetailsDto,
                                              Object hotelContentDto) throws Throwable {
        log.info("AROUND before - invoked create() method in: {}, with params: {}, {}, {}", service, hotelDto, hotelDetailsDto, hotelContentDto);
        try {
            Object result = joinPoint.proceed();
            log.info("AROUND after returning - invoked create() method in: {}, result: {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.warn("AROUND after throwing - invoked create() method in: {}, exception: {}", service, ex);
            throw ex;
        } finally {
            log.info("AROUND after (finally) - invoked create() method in: {}", service);
        }
    }

    /**
     * Logging update() method in HotelService
     */
    @Around(value = "isHotelService()" +
                    "&& isUpdateMethodInHotelService() " +
                    "&& target(service) " +
                    "&& args(hotelDto, hotelDetailsDto, hotelContentDto)",
            argNames = "joinPoint,service,hotelDto,hotelDetailsDto,hotelContentDto")
    public Object addLoggingAroundUpdateHotel(ProceedingJoinPoint joinPoint,
                                              Object service,
                                              Object hotelDto,
                                              Object hotelDetailsDto,
                                              Object hotelContentDto) throws Throwable {
        log.info("AROUND before - invoked update() method in: {}, with params: {}, {}, {}", service, hotelDto, hotelDetailsDto, hotelContentDto);
        try {
            Object result = joinPoint.proceed();
            log.info("AROUND after returning - invoked update() method in: {}, result: {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.warn("AROUND after throwing - invoked update() method in: {}, exception: {}", service, ex);
            throw ex;
        } finally {
            log.info("AROUND after (finally) - invoked update() method in: {}", service);
        }
    }
}