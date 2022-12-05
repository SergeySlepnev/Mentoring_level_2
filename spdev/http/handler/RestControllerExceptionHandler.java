package com.spdev.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.spdev.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

}
