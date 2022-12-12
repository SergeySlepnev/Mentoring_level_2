package com.spdev.validation.impl;

import com.spdev.service.ApplicationContentService;
import com.spdev.validation.CheckContentSize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ContentSizeValidator implements ConstraintValidator<CheckContentSize, MultipartFile> {

    private final ApplicationContentService applicationContentService;

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return applicationContentService.isAllowedSize(value.getOriginalFilename());
    }
}