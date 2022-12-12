package com.spdev.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@Slf4j
@ControllerAdvice(basePackages = "com.spdev.http")
public class ControllerExceptionHandler {

    //Этот handler не хочет отлавливать SizeLimitExceededException
    //И приложение падает при загрузке большого файла.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SizeLimitExceededException.class)
    public String handleException(SizeLimitExceededException exception) {
        log.error("${error._500}", exception);

        return "error/error500";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(LocalDate.class, new StringTrimmerEditor(true));
    }
}