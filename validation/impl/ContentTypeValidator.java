package com.spdev.validation.impl;

import com.spdev.service.ApplicationContentService;
import com.spdev.validation.CheckContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ContentTypeValidator implements ConstraintValidator<CheckContentType, MultipartFile> {

    private final ApplicationContentService applicationContentService;

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return applicationContentService.isContentValid(value.getOriginalFilename());
    }
}